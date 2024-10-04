package com.sscanner.team.products.service;

import com.sscanner.team.products.entity.Product;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.products.entity.ProductImg;
import com.sscanner.team.products.repository.ProductRepository;
import com.sscanner.team.products.responsedto.ProductUploadImgResponseDto;
import com.sscanner.team.products.responsedto.ProductWithImgResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImgService productImgService;

    @Override
    public Map<String, Object> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        List<ProductWithImgResponseDto> productWithImgDtos = new ArrayList<>();

        for (Product product : products) {
            List<ProductImg> productImgs = productImgService.getAllProductImgs(product.getId());
            ProductWithImgResponseDto dto = ProductWithImgResponseDto.of(product, productImgs);
            productWithImgDtos.add(dto);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("products", productWithImgDtos);
        response.put("currentPage", products.getNumber() + 1);
        response.put("totalItems", products.getTotalElements());
        response.put("totalPages", products.getTotalPages());

        return response;
    }

    @Override
    public ProductWithImgResponseDto  getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PRODUCT_ID));
        List<ProductImg> productImgs = productImgService.getAllProductImgs(productId);
        return ProductWithImgResponseDto.of(product, productImgs);
    }

    @Override
    public List<ProductUploadImgResponseDto> addProductImages(Long productId, List<MultipartFile> files) {
        // 이미지 업로드 처리 후 DTO로 변환하여 반환
        return productImgService.uploadProductImages(productId, files)
                .stream()
                .map(ProductUploadImgResponseDto::of)
                .toList();
    }
}
