package com.sscanner.team.products.responsedto;

import com.sscanner.team.products.entity.Product;

public record ProductResponseDto(
    Long productId,
    String productName,
    Integer price
) {
    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

    public Product toEntity() {
        return Product.builder()
                .id(productId)  // 엔티티의 id와 매핑
                .name(productName)
                .price(price)
                .build();
    }
}
