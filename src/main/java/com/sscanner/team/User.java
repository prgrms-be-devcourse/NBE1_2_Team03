package com.sscanner.team;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Entity
@Table(name = "USER")
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id", nullable = false, length = 16)
    private String userId;

    @Column(name = "email", nullable = false, length = 20)
    private String email;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "authority", nullable = false, length = 10)
    private String authority;

}