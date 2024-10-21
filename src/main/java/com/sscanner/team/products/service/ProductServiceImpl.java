package com.sscanner.team.products.service;

import com.sscanner.team.products.entity.Product;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.products.repository.ProductRepository;
import com.sscanner.team.products.responsedto.ProductImgResponseDto;
import com.sscanner.team.products.responsedto.ProductResponseDto;
import com.sscanner.team.products.responsedto.ProductWithImgResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImgService productImgService;

    // product & productImg n+1 문제 고려
    @Override
    public Map<String, Object> findAllWithImgs(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        // 모든 Product의 ID 리스트를 추출
        List<Long> productIds = extractProductIds(products);

        // Product ID 리스트를 사용해 한 번에 관련 이미지를 조회
        Map<Long, List<ProductImgResponseDto>> productImgDtosMap = productImgService.findAllByProductIds(productIds);

        // 각 Product에 해당하는 이미지를 DTO로 변환
        List<ProductWithImgResponseDto> productWithImgDtos = new ArrayList<>();
        for (Product product : products) {
            List<ProductImgResponseDto> productImgDtos = productImgDtosMap.getOrDefault(product.getId(), Collections.emptyList());
            productWithImgDtos.add(toProductWithImgDto(product, productImgDtos));
        }

        return createResponse(productWithImgDtos, products);
    }

    private List<Long> extractProductIds(Page<Product> products) {
        return products.stream()
                .map(Product::getId)
                .toList();
    }

    private ProductWithImgResponseDto toProductWithImgDto(Product product, List<ProductImgResponseDto> productImgDtos) {
        List<String> imgUrls = productImgDtos.stream()
                .map(ProductImgResponseDto::url)
                .toList();

        return ProductWithImgResponseDto.from(product, imgUrls);
    }

    private Map<String, Object> createResponse(List<ProductWithImgResponseDto> productWithImgDtos, Page<Product> products) {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productWithImgDtos);
        response.put("currentPage", products.getNumber() + 1);
        response.put("totalItems", products.getTotalElements());
        response.put("totalPages", products.getTotalPages());

        return response;
    }

    @Override
    public ProductResponseDto findById(Long productId) {
        return productRepository.findById(productId)
                .map(ProductResponseDto::from)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PRODUCT_ID));
    }

    @Override
    public ProductWithImgResponseDto findWithImgById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PRODUCT_ID));
        List<ProductImgResponseDto> productImgs = productImgService.findByProductId(productId);
        return toProductWithImgDto(product, productImgs);
    }

    @Transactional
    @Override
    public List<ProductImgResponseDto> addImages(Long productId, List<MultipartFile> files) {
        // 이미지 업로드 처리 후 DTO로 변환하여 반환
        return productImgService.uploadImages(productId, files)
                .stream()
                .toList();
    }
}
