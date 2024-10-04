package com.sscanner.team.products.entity;


import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Getter
@Entity
@Table(name = "Product_img")
@NoArgsConstructor(access = PROTECTED)
public class ProductImg extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_id", nullable = false)
    private long id;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @Column(name = "product_img_url", nullable = false)
    private String productImgUrl;

    @Builder
    public ProductImg(long productId, String productImgUrl) {
        this.productId = productId;
        this.productImgUrl = productImgUrl;
    }
}
