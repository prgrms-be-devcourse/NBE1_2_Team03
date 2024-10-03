package com.sscanner.team.board.responsedto;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashCategory;

public record BoardListResponseDTO(
        Long id,
        BoardCategory boardCategory,
        String roadNameAddress,
        String detailedAddress,
        TrashCategory trashCategory
) {
    public static BoardListResponseDTO from(Board board) {
        return new BoardListResponseDTO(
                board.getId(),
                board.getBoardCategory(),
                board.getRoadNameAddress(),
                board.getDetailedAddress(),
                board.getTrashCategory()
        );
    }
}
