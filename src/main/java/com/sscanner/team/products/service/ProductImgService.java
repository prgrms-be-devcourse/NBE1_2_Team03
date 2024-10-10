package com.sscanner.team.products.service;

import com.sscanner.team.products.entity.ProductImg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductImgService {
    List<ProductImg> getProductImgsbyId(Long productId);
    Map<Long, List<ProductImg>> getAllProductImgsByIds(List<Long> productIds);
    List<ProductImg> uploadProductImages(Long productId, List<MultipartFile> files);
}
