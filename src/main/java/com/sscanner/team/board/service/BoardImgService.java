package com.sscanner.team.board.service;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardImgService {
    List<BoardImg> saveBoardImg(Board board, List<MultipartFile> files);
}
