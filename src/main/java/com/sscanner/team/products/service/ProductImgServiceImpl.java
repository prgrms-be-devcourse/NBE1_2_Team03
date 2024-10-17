package com.sscanner.team.products.service;

import com.sscanner.team.global.common.service.ImageService;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.products.entity.ProductImg;
import com.sscanner.team.products.repository.ProductImgRepository;
import com.sscanner.team.products.responsedto.ProductImgResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductImgServiceImpl implements ProductImgService {

    private final ProductImgRepository productImgRepository;
    private final ImageService imageService;

    @Override
    public List<ProductImgResponseDto> findByProductId(Long productId) {
        List<ProductImg> productImgs = productImgRepository.findAllByProductId(productId);

        return productImgs.stream()
                .map(ProductImgResponseDto::from)
                .toList();
    }

    @Override
    public Map<Long, List<ProductImgResponseDto>> findAllByProductIds(List<Long> productIds) {
        List<ProductImg> productImgs = productImgRepository.findAllByProductIdIn(productIds);

        Map<Long, List<ProductImgResponseDto>> productImgDtosMap = new HashMap<>();

        for (ProductImg productImg : productImgs) {
            Long productId = productImg.getProductId();
            productImgDtosMap.computeIfAbsent(productId, k -> new ArrayList<>()).add(ProductImgResponseDto.from(productImg));
        }

        return productImgDtosMap;
    }

    @Transactional
    @Override
    public List<ProductImgResponseDto> uploadImages(Long productId, List<MultipartFile> files) {
        List<ProductImgResponseDto> productImgs = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String imgUrl = makeImgUrl(file);
                ProductImg saveProductImg = saveProductImg(productId, imgUrl);
                productImgs.add(ProductImgResponseDto.from(saveProductImg));
            }
        } catch (Exception e) {
            throw new BadRequestException(ExceptionCode.FILE_UPLOAD_FAIL);
        }

        return productImgs;
    }

    private ProductImg saveProductImg(Long productId, String imgUrl) {
        ProductImg productImg = ProductImg.builder()
                .productId(productId)
                .url(imgUrl)
                .build();

        return productImgRepository.save(productImg);
    }

    private String makeImgUrl(MultipartFile file) {
        return imageService.makeImgUrl(file);
    }

    @Override
    public String findMainImageUrl(Long productId) {
        return productImgRepository.findAllByProductId(productId).stream()
                .findFirst()
                .map(ProductImg::getUrl)
                .orElse(null);
    }
}
