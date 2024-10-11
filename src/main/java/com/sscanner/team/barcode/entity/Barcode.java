package com.sscanner.team.barcode.entity;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "barcode")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Barcode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "barcode_url", nullable = false)
    private String barcodeUrl;

    @Builder
    public Barcode(String userId, Long productId, String barcodeUrl) {
        this.userId = userId;
        this.productId = productId;
        this.barcodeUrl = barcodeUrl;
    }
}
