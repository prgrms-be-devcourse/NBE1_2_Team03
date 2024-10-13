package com.sscanner.team.products.service;

import com.sscanner.team.global.common.service.ImageService;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.products.entity.ProductImg;
import com.sscanner.team.products.repository.ProductImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductImgServiceImpl implements ProductImgService {

    private final ProductImgRepository productImgRepository;
    private final ImageService imageService;

    @Override
    public List<ProductImg> getProductImgsbyId(Long productId) {
        return productImgRepository.findAllByProductId(productId);
    }

    @Override
    public Map<Long, List<ProductImg>> getAllProductImgsByIds(List<Long> productIds) {
        List<ProductImg> productImgs = productImgRepository.findAllByProductIdIn(productIds);

        Map<Long, List<ProductImg>> productImgsMap = new HashMap<>();

        for (ProductImg productImg : productImgs) {
            Long productId = productImg.getProductId();

            productImgsMap.computeIfAbsent(productId, k -> new ArrayList<>()).add(productImg);
        }

        return productImgsMap;
    }

    @Override
    public List<ProductImg> uploadProductImages(Long productId, List<MultipartFile> files) {
        List<ProductImg> productImgList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String imgUrl = imageService.makeImgUrl(file);
                ProductImg productImg = ProductImg.builder()
                        .productId(productId)
                        .productImgUrl(imgUrl)
                        .build();
                productImgList.add(productImgRepository.save(productImg));
            } catch (Exception e) {
                throw new BadRequestException(ExceptionCode.FILE_UPLOAD_FAIL);
            }
        }

        return productImgList;
    }

    @Override
    public String getRepresentativeProductImgUrl(Long productId) {
        List<ProductImg> productImgs = productImgRepository.findAllByProductId(productId);

        // 대표 이미지가 없다면 첫 번째 이미지를 사용
        return productImgs.isEmpty() ? null : productImgs.get(0).getProductImgUrl();
    }
}
