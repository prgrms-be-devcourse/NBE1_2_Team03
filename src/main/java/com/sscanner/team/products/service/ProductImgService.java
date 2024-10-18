package com.sscanner.team.products.service;

import com.sscanner.team.products.entity.ProductImg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductImgService {
    List<ProductImg> findAllByProductId(Long productId);
    Map<Long, List<ProductImg>> findProductImgsGroupedByProductId(List<Long> productIds);
    List<ProductImg> uploadImages(Long productId, List<MultipartFile> files);
    String findMainImageUrl(Long productId);
}
