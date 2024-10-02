package com.sscanner.team.points.requestdto;

public record PointPaymentRequestDto(
        String userId,
        Long productId
) {
}
