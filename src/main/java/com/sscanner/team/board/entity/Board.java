package com.sscanner.team.board.entity;

import com.sscanner.team.User;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.global.common.BaseEntity;
import com.sscanner.team.trashcan.type.TrashcanStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Board extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "trashcan_id")
    private Long trashcanId;

    @Column(name = "board_category", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @Lob
    @Column(name = "significants")
    private String significant;

    @Column(name = "updated_trashcan_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrashcanStatus updatedTrashcanStatus;

    @Column(name = "give_point", nullable = false)
    private Boolean givePoint;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardImg> boardImgs = new ArrayList<>();

    @Builder
    public Board(User user,
                 Long trashcanId,
                 BoardCategory boardCategory,
                 String significant,
                 TrashcanStatus updatedTrashcanStatus) {
        this.user = user;
        this.trashcanId = trashcanId;
        this.boardCategory = boardCategory;
        this.significant = significant;
        this.updatedTrashcanStatus = updatedTrashcanStatus;
        this.givePoint = false;
    }

    public void addBoardImgs(List<BoardImg> boardImgs) {
        for(BoardImg boardImg : boardImgs) {
            this.boardImgs.add(boardImg);
        }
    }
}