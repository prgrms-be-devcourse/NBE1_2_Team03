package com.sscanner.team.board.service;

import com.sscanner.team.board.requestdto.BoardCreateRequestDTO;
import com.sscanner.team.board.responsedto.BoardCreateResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    BoardCreateResponseDTO createBoard(BoardCreateRequestDTO boardCreateRequestDTO,
                                          List<MultipartFile> files);
    void deleteBoard(Long boardId);
}
