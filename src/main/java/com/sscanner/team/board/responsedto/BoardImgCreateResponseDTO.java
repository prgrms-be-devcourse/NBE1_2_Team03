package com.sscanner.team.board.responsedto;

import com.sscanner.team.board.entity.BoardImg;

public record BoardImgCreateResponseDTO(
        String boardImgUrl
) {
    public static BoardImgCreateResponseDTO of(BoardImg boardImg) {
        return new BoardImgCreateResponseDTO(
                boardImg.getBoardImgUrl());
    }
}
