package com.sscanner.team.board.service;

import com.sscanner.team.board.entity.Board;
import org.springframework.web.multipart.MultipartFile;

public interface BoardImgService {
    public void saveBoardImg(Board board, MultipartFile file);
    public String makeImgUrl(MultipartFile file);
}
