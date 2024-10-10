package com.sscanner.team.products.responsedto;

import com.sscanner.team.products.entity.ProductImg;

public record ProductUploadImgResponseDto(
        Long productImgId,
        String productImgUrl
) {
    public static ProductUploadImgResponseDto of(ProductImg productImg) {
        return new ProductUploadImgResponseDto(
                productImg.getId(),
                productImg.getProductImgUrl()
        );
    }
}
