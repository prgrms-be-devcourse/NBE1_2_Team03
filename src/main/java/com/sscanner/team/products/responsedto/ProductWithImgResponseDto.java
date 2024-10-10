package com.sscanner.team.products.responsedto;

import com.sscanner.team.products.entity.Product;
import com.sscanner.team.products.entity.ProductImg;

import java.util.List;

public record ProductWithImgResponseDto(
        Long productId,
        String productName,
        Integer price,
        List<String> imgUrls
) {
    public static ProductWithImgResponseDto of(Product product, List<ProductImg> productImgs) {
        List<String> imgUrls = productImgs.stream()
                .map(ProductImg::getProductImgUrl)
                .toList();

        return new ProductWithImgResponseDto(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                imgUrls
        );
    }
}
