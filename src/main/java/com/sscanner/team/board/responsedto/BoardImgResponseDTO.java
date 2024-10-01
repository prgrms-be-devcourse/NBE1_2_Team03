package com.sscanner.team.board.responsedto;

import com.sscanner.team.board.entity.BoardImg;

public record BoardImgResponseDTO(
        String boardImgUrl
) {
    public static BoardImgResponseDTO of(BoardImg boardImg) {
        return new BoardImgResponseDTO(
                boardImg.getBoardImgUrl());
    }
}
