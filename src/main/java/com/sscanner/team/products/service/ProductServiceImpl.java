package com.sscanner.team.products.service;

import com.sscanner.team.Product;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.products.repository.ProductRepository;
import com.sscanner.team.products.responsedto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Map<String, Object> getAllProducts(Pageable pageable) {
        Page<ProductResponseDto> products = productRepository.findAll(pageable).map(ProductResponseDto::of);

        Map<String, Object> response = new HashMap<>();
        response.put("products", products.getContent());
        response.put("currentPage", products.getNumber() + 1);
        response.put("totalItems", products.getTotalElements());
        response.put("totalPages", products.getTotalPages());

        return response;
    }

    @Override
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PRODUCT_ID));
        return ProductResponseDto.of(product);
    }
}
