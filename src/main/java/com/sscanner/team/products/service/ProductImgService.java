package com.sscanner.team.products.service;

import com.sscanner.team.products.entity.ProductImg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImgService {
    List<ProductImg> getAllProductImgs(Long productId);
    ProductImg getProductImgById(Long productImgId);
    List<ProductImg> uploadProductImages(Long productId, List<MultipartFile> files);
}
