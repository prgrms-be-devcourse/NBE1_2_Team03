package com.sscanner.team.board.entity;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "Board_img")
@NoArgsConstructor(access = PROTECTED)
public class BoardImg extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_img_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "board_img_url", nullable = false)
    private String boardImgUrl;

    @Builder
    public BoardImg(Board board, String boardImgUrl) {
        this.board = board;
        this.boardImgUrl = boardImgUrl;
    }

    public static BoardImg makeBoardImg(Board board, String boardImgUrl) {
        return BoardImg.builder()
                .board(board)
                .boardImgUrl(boardImgUrl)
                .build();
    }
}