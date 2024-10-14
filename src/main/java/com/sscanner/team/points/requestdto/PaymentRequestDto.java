package com.sscanner.team.points.requestdto;

import com.sscanner.team.PaymentRecord;
import com.sscanner.team.Product;
import com.sscanner.team.user.entity.User;

import java.util.UUID;

public record PaymentRequestDto(
        UUID paymentRecordId,
        String userId,
        Long productId,
        Integer payment
) {
    // DTO에서 엔티티로 변환하는 메서드
    public PaymentRecord toEntity(User user, Product product) {
        return PaymentRecord.builder()
                .paymentRecordId(paymentRecordId)
                .user(user)
                .product(product)
                .payment(payment)
                .build();
    }

    public static PaymentRequestDto of(User user, Product product, Integer payment) {
        return new PaymentRequestDto(UUID.randomUUID(), user.getUserId(), product.getId(), payment);
    }
}
