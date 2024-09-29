package com.sscanner.team.board.requestdto;

import com.sscanner.team.User;
import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AddBoardCreateRequestDTO(
        @NotNull(message = "위도는 필수입니다.")
        @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다.")
        @DecimalMax(value = "90.0", message = "위도는 90 이하이어야 합니다.")
        BigDecimal latitude,

        @NotNull(message = "경도는 필수입니다.")
        @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다.")
        @DecimalMax(value = "180.0", message = "경도는 180 이하이어야 합니다.")
        BigDecimal longitude,

        @NotBlank(message = "도로명 주소는 필수입니다.")
        String roadNameAddress,

        String detailedAddress,

        @NotNull(message = "쓰레기통 유형은 필수입니다.")
        TrashCategory trashCategory,

        @NotNull(message = "쓰레기통 상태 작성은 필수입니다.")
        TrashcanStatus trashcanStatus,

        @NotNull(message = "게시판 유형 작성은 필수입니다.")
        BoardCategory boardCategory,

        String significant,

        @NotNull(message = "userId는 필수입니다.")
        String userId
) {
        public Trashcan toEntityTrashcan() {
                return Trashcan.builder()
                        .latitude(this.latitude())
                        .longitude(this.longitude())
                        .roadNameAddress(this.roadNameAddress())
                        .detailedAddress(this.detailedAddress())
                        .trashCategory(this.trashCategory())
                        .build();
        }

        public Board toEntityAddBoard(User user, Long trashcanId) {
                return Board.builder()
                        .user(user)
                        .trashcanId(trashcanId)
                        .boardCategory(this.boardCategory())
                        .significant(this.significant())
                        .updatedTrashcanStatus(this.trashcanStatus())
                        .build();
        }
}
