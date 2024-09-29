package com.sscanner.team.board.requestdto;

import com.sscanner.team.User;
import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;
import jakarta.validation.constraints.NotNull;

public record EctBoardCreateRequestDTO(
        @NotNull(message = "userId는 필수입니다.")
        String userId,

        @NotNull(message = "userId는 필수입니다.")
        Long trashcanId,

        @NotNull(message = "게시판 유형 작성은 필수입니다.")
        BoardCategory boardCategory,

        String significant,

        @NotNull(message = "쓰레기통 상태 작성은 필수입니다.")
        TrashcanStatus trashcanStatus
) {
        public Board toEntityEctBoard(User user) {
                return Board.builder()
                        .user(user)
                        .trashcanId(this.trashcanId())
                        .boardCategory(this.boardCategory())
                        .significant(this.significant())
                        .updatedTrashcanStatus(this.trashcanStatus())
                        .build();
        }
}
