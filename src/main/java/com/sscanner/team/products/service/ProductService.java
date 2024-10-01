package com.sscanner.team.products.service;

import com.sscanner.team.products.responsedto.ProductResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ProductService {
    Map<String, Object> getAllProducts(Pageable pageable);
    ProductResponseDto getProductById(Long productId);
}
