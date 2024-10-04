package com.sscanner.team.products.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.products.responsedto.ProductUploadImgResponseDto;
import com.sscanner.team.products.responsedto.ProductWithImgResponseDto;
import com.sscanner.team.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
     * 이미지도 함께 반환합니다.
     * @param productId 상품 ID
     * @return 상품 정보 조회 성공 메시지
     */
    @GetMapping("/{productId}")
    public ApiResponse<ProductWithImgResponseDto> getProductById(@PathVariable Long productId) {
        ProductWithImgResponseDto product = productService.getProductById(productId);
        return ApiResponse.ok(200, product, "상품 정보 조회 성공");
    }

    /**
     * 상품 이미지 등록 API
     * @param productId 상품 ID
     * @param files 이미지 파일들 (다중 업로드 가능)
     * @return 업로드된 이미지 정보 목록
     */
    @PostMapping("/{productId}/images")
    public ApiResponse<List<ProductUploadImgResponseDto>> uploadProductImages(
            @PathVariable Long productId,
            @RequestParam("images") List<MultipartFile> files
    ) {
        List<ProductUploadImgResponseDto> uploadedImages = productService.addProductImages(productId, files);
        return ApiResponse.ok(200, uploadedImages, "이미지 등록 성공");
    }
}
