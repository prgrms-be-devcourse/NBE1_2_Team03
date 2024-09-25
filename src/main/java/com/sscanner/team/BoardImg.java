package com.sscanner.team;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Entity
@Table(name = "Board_img")
public class BoardImg extends BaseEntity {
    @Id
    @Column(name = "board_img_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "board_img_url", nullable = false)
    private String boardImgUrl;

}