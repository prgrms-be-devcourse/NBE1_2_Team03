package com.sscanner.team.products.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.products.responsedto.ProductResponseDto;
import com.sscanner.team.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();

        return ApiResponse.ok(200, products, "상품 목록 조회 성공");
    }
}
