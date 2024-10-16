package com.sscanner.team.products.service;

import com.sscanner.team.products.responsedto.ProductImgResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductImgService {
    List<ProductImgResponseDto> findByProductId(Long productId);
    Map<Long, List<ProductImgResponseDto>> findAllByProductIds(List<Long> productIds);
    List<ProductImgResponseDto> uploadImages(Long productId, List<MultipartFile> files);
    String findMainImageUrl(Long productId);
}
