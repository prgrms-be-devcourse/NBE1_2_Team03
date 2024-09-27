package com.sscanner.team.products.service;

import com.sscanner.team.products.repository.ProductRepository;
import com.sscanner.team.products.responsedto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponseDto(product.getId(), product.getProductName(), product.getPrice()))
                .toList();
    }
}
