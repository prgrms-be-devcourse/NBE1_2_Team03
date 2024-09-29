package com.sscanner.team;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity
@Table(name = "User_point")
public class UserPoint extends BaseEntity {
    @Id
    @Column(name = "user_point_id", nullable = false, length = 16)
    private String userPointId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "point", nullable = false)
    private Integer point;


}