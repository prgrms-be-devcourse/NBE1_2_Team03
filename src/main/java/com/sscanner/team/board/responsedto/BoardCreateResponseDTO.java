package com.sscanner.team.board.responsedto;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;

import java.util.List;
import java.util.stream.Collectors;

public record BoardCreateResponseDTO(
    Long id,
    Long trashcanId,
    BoardCategory boardCategory,
    String significant,
    TrashcanStatus updatedTrashcanStatus,
    List<BoardImgCreateResponseDTO> boardImgs
) {
    public static BoardCreateResponseDTO from(Board board) {
        return new BoardCreateResponseDTO(
                board.getId(),
                board.getTrashcanId(),
                board.getBoardCategory(),
                board.getSignificant(),
                board.getUpdatedTrashcanStatus(),
                board.getBoardImgs().stream()
                        .map((BoardImg boardImg) -> BoardImgCreateResponseDTO.of(boardImg))
                        .collect(Collectors.toList())
        );
    }
}
