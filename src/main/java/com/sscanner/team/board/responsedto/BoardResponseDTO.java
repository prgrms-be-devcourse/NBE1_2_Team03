package com.sscanner.team.board.responsedto;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record BoardResponseDTO(
    Long id,
    BoardCategory boardCategory,
    String significant,
    Long trashcanId,
    BigDecimal latitude,
    BigDecimal longitude,
    String roadNameAddress,
    String detailedAddress,
    TrashCategory trashCategory,
    TrashcanStatus updatedTrashcanStatus,
    List<BoardImgResponseDTO> boardImgs
) {
    public static BoardResponseDTO from(Board board, List<BoardImg> boardImgs) {
        return new BoardResponseDTO(
                board.getId(),
                board.getBoardCategory(),
                board.getSignificant(),
                board.getTrashcanId(),
                board.getLatitude(),
                board.getLongitude(),
                board.getRoadNameAddress(),
                board.getDetailedAddress(),
                board.getTrashCategory(),
                board.getUpdatedTrashcanStatus(),
                boardImgs.stream()
                        .map((BoardImg boardImg) -> BoardImgResponseDTO.of(boardImg))
                        .collect(Collectors.toList())
        );
    }
}
