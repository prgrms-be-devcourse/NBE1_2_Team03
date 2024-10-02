package com.sscanner.team.products.entity;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Product extends BaseEntity {
    @Id
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 30)
    private String productName;

    @Column(name = "price", nullable = false)
    private Integer price;

}
