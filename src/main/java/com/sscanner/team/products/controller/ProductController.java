package com.sscanner.team.products.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.products.responsedto.ProductResponseDto;
import com.sscanner.team.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 페이징 처리를 한 모든 상품 조회 API
     * @param page 페이지
     * @param size 보여줄 상품 개수
     * @return 상품 목록 조회 성공 메시지
     */
    @GetMapping
    public ApiResponse<Map<String, Object>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Map<String, Object> response = productService.getAllProducts(pageable);

        return ApiResponse.ok(200, response, "상품 목록 조회 성공");
    }

    /**
     * 단일 상품 정보 조회 API
     * @param productId 상품 ID
     * @return 상품 정보 조회 성공 메시지
     */
    @GetMapping("/{productId}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable Long productId) {
        ProductResponseDto product = productService.getProductById(productId);
        return ApiResponse.ok(200, product, "상품 정보 조회 성공");
    }
}
