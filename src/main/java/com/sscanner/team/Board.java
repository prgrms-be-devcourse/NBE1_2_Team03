package com.sscanner.team;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Entity
public class Board extends BaseEntity {
    @Id
    @Column(name = "board_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trashcan_id", nullable = false)
    private Trashcan trashcan;

    @Column(name = "board_category", nullable = false, length = 10)
    private String boardCategory;

    @Lob
    @Column(name = "significant")
    private String significant;

    @ColumnDefault("'EMPTY'")
    @Column(name = "updated_trashcan_status", nullable = false, length = 10)
    private String updatedTrashcanStatus;

    @ColumnDefault("0")
    @Column(name = "give_point", nullable = false)
    private Boolean givePoint = false;

}