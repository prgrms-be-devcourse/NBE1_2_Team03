package com.sscanner.team;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "USER")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, length = 36, updatable = false)
    private String userId;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "authority", nullable = false, length = 10)
    private String authority;


    @Builder
    public User( String email, String password, String nickname, String phone, String authority) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.authority = authority;
    }

}