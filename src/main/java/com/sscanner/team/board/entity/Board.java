package com.sscanner.team.board.entity;

import com.sscanner.team.User;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.global.common.BaseEntity;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    @Column(name = "board_category", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @Lob
    @Column(name = "significants")
    private String significant;

    @Column(name = "give_point", nullable = false)
    private Boolean givePoint;

    @Column(name = "trashcan_id")
    private Long trashcanId;

    @Column(name = "latitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "road_name_address", nullable = false, length = 100)
    private String roadNameAddress;

    @Column(name = "detailed_address", nullable = false, length = 100)
    private String detailedAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "trash_category", nullable = false, length = 15)
    private TrashCategory trashCategory;

    @Column(name = "updated_trashcan_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrashcanStatus updatedTrashcanStatus;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardImg> boardImgs = new ArrayList<>();

    @Builder
    public Board(User user,
                 BoardCategory boardCategory,
                 String significant,
                 Long trashcanId,
                 BigDecimal latitude,
                 BigDecimal longitude,
                 String roadNameAddress,
                 String detailedAddress,
                 TrashCategory trashCategory,
                 TrashcanStatus updatedTrashcanStatus) {
        this.user = user;
        this.boardCategory = boardCategory;
        this.significant = significant;
        this.givePoint = false;
        this.trashcanId = trashcanId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roadNameAddress = roadNameAddress;
        this.detailedAddress = detailedAddress;
        this.trashCategory = trashCategory;
        this.updatedTrashcanStatus = updatedTrashcanStatus;
    }

    public void addBoardImgs(List<BoardImg> boardImgs) {
        for(BoardImg boardImg : boardImgs) {
            this.boardImgs.add(boardImg);
        }
    }
}