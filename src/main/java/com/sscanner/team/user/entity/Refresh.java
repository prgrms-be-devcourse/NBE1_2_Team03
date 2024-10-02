package com.sscanner.team.user.entity;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Refresh extends BaseEntity { // 토큰 저장 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String refresh;
    private String expiration;

    @Builder
    public Refresh(String email, String refresh, String expiration) {
        this.email = email;
        this.refresh = refresh;
        this.expiration = expiration;
    }

}





