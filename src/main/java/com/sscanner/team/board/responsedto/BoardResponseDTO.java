package com.sscanner.team.board.responsedto;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;

import java.util.List;
import java.util.stream.Collectors;

public record BoardResponseDTO(
    Long id,
    Long trashcanId,
    BoardCategory boardCategory,
    String significant,
    TrashcanStatus updatedTrashcanStatus,
    List<BoardImgResponseDTO> boardImgs
) {
    public static BoardResponseDTO from(Board board, List<BoardImg> boardImgs) {
        return new BoardResponseDTO(
                board.getId(),
                board.getTrashcanId(),
                board.getBoardCategory(),
                board.getSignificant(),
                board.getUpdatedTrashcanStatus(),
                boardImgs.stream()
                        .map((BoardImg boardImg) -> BoardImgResponseDTO.of(boardImg))
                        .collect(Collectors.toList())
        );
    }
}
