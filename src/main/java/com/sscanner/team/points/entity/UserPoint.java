package com.sscanner.team.points.entity;

import com.sscanner.team.User;
import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@Table(name = "User_point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPoint extends BaseEntity {
    @Id
    @Column(name = "user_point_id", nullable = false, length = 16)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Builder
    public UserPoint(String id, User user, Integer point) {
        this.id = id;
        this.user = user;
        this.point = point;
    }
}
