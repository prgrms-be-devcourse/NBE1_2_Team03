package com.sscanner.team.products.responsedto;

public record ProductResponseDto(
    Long productId,
    String productName,
    Integer price
) {}
