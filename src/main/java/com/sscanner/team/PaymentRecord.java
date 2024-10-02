package com.sscanner.team;

import com.sscanner.team.global.common.BaseEntity;
import com.sscanner.team.util.UUIDConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "Payment_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentRecord extends BaseEntity {
    @Id
    @Convert(converter = UUIDConverter.class)
    @Column(name = "payment_record_id", nullable = false, length = 16)
    private UUID paymentRecordId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "payment", nullable = false)
    private Integer payment;

    @Builder
    public PaymentRecord(UUID paymentRecordId, User user, Product product, Integer payment) {
        this.paymentRecordId = paymentRecordId;
        this.user = user;
        this.product = product;
        this.payment = payment;
    }
}
