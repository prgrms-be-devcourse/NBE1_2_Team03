package com.sscanner.team.products.responsedto;

import com.sscanner.team.products.entity.Product;

public record ProductResponseDto(
    Long productId,
    String productName,
    Integer price
) {
    public static ProductResponseDto of(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getProductName(),
                product.getPrice()
        );
    }
}
