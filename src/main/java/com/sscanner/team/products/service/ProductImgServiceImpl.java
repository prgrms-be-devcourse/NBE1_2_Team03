package com.sscanner.team.products.service;

import com.sscanner.team.global.common.service.ImageService;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.products.entity.ProductImg;
import com.sscanner.team.products.repository.ProductImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImgServiceImpl implements ProductImgService {

    private final ProductImgRepository productImgRepository;
    private final ImageService imageService;

    @Override
    public List<ProductImg> getAllProductImgs(Long productId) {
        return productImgRepository.findAllByProductId(productId);
    }

    @Override
    public ProductImg getProductImgById(Long productImgId) {
        return productImgRepository.findById(productImgId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PRODUCT_IMG_ID));
    }

    @Override
    public List<ProductImg> uploadProductImages(Long productId, List<MultipartFile> files) {
        List<ProductImg> productImgList = new ArrayList<>();

        for (MultipartFile file : files) {
            String imgUrl = imageService.makeImgUrl(file);
            ProductImg productImg = ProductImg.builder()
                    .productId(productId)
                    .productImgUrl(imgUrl)
                    .build();
            productImgList.add(productImgRepository.save(productImg));
        }

        return productImgList;
    }
}
