package com.sscanner.team.products.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.products.responsedto.ProductResponseDto;
import com.sscanner.team.products.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping
    public ApiResponse<Map<String, Object>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProductResponseDto> products = productService.getAllProducts(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("products", products.getContent());
        response.put("currentPage", products.getNumber() + 1);
        response.put("totalItems", products.getTotalElements());
        response.put("totalPages", products.getTotalPages());

        return ApiResponse.ok(200, response, "상품 목록 조회 성공");
    }
}
