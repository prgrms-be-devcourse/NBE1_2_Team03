package com.sscanner.team.products.entity;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "Product_QR")
public class ProductQr extends BaseEntity {
    @Id
    @Column(name = "product_QR_id", nullable = false, length = 16)
    private String productQrId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;


}