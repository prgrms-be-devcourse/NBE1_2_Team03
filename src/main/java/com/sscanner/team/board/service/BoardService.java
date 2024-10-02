package com.sscanner.team.board.service;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.requestdto.BoardCreateRequestDTO;
import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO;
import com.sscanner.team.board.responsedto.BoardResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    BoardResponseDTO createBoard(BoardCreateRequestDTO boardCreateRequestDTO,
                                 List<MultipartFile> files);
    void deleteBoard(Long boardId);
    BoardResponseDTO updateBoard(Long boardId,
                                 BoardUpdateRequestDTO boardUpdateRequestDTO,
                                 List<MultipartFile> files);

    Board getBoard(Long boardId);
}
