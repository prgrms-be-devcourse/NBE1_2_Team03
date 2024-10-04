package com.sscanner.team.products.service;

import com.sscanner.team.products.responsedto.ProductUploadImgResponseDto;
import com.sscanner.team.products.responsedto.ProductWithImgResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Map<String, Object> getAllProducts(Pageable pageable);
    ProductWithImgResponseDto getProductById(Long productId);
    List<ProductUploadImgResponseDto> addProductImages(Long productId, List<MultipartFile> files);
}
