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
    public Map<String, Object> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        // 모든 Product의 ID 리스트를 추출
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .toList();

        // Product ID 리스트를 사용해 한 번에 관련 이미지를 조회
        Map<Long, List<ProductImg>> productImgsMap = productImgService.getAllProductImgsByIds(productIds);

        // 각 Product에 해당하는 이미지를 DTO로 변환
        List<ProductWithImgResponseDto> productWithImgDtos = new ArrayList<>();
        for (Product product : products) {
            List<ProductImg> productImgs = Optional.ofNullable(productImgsMap.get(product.getId())).orElse(Collections.emptyList());
            productWithImgDtos.add(createProductWithImgDto(product, productImgs));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("products", productWithImgDtos);
        response.put("currentPage", products.getNumber() + 1);
        response.put("totalItems", products.getTotalElements());
        response.put("totalPages", products.getTotalPages());

        return response;
    }

    private ProductWithImgResponseDto createProductWithImgDto(Product product, List<ProductImg> productImgs) {
        return ProductWithImgResponseDto.of(product, productImgs);
    }

    @Override
    public Product getProductEntityById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PRODUCT_ID));
    }

    @Override
    public ProductWithImgResponseDto getProductWithImgById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PRODUCT_ID));
        List<ProductImg> productImgs = productImgService.getProductImgsbyId(productId);
        return createProductWithImgDto(product, productImgs);
    }

    @Transactional
    @Override
    public List<ProductUploadImgResponseDto> addProductImages(Long productId, List<MultipartFile> files) {
        // 이미지 업로드 처리 후 DTO로 변환하여 반환
        return productImgService.uploadProductImages(productId, files)
                .stream()
                .map(ProductUploadImgResponseDto::of)
                .toList();
    }
}
