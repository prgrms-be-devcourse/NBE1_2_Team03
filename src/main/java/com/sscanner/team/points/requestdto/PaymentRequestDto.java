package com.sscanner.team.points.requestdto;

import com.sscanner.team.points.entity.PaymentRecord;
import com.sscanner.team.products.entity.Product;
import com.sscanner.team.User;

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
