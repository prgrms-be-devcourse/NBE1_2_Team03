package com.sscanner.team.products.service;

import com.sscanner.team.products.repository.ProductRepository;
import com.sscanner.team.products.responsedto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> new ProductResponseDto(product.getId(), product.getProductName(), product.getPrice()));
    }
}
