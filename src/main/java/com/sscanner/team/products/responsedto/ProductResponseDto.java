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
                product.getName(),
                product.getPrice()
        );
    }

    public Product toEntity() {
        return Product.builder()
                .id(this.productId)  // 엔티티의 id와 매핑
                .name(this.productName)
                .price(this.price)
                .build();
    }
}
