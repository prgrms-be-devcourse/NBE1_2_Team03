package com.sscanner.team.board.service;

import com.sscanner.team.board.requestdto.AddBoardCreateRequestDTO;
import com.sscanner.team.board.requestdto.EctBoardCreateRequestDTO;
import com.sscanner.team.board.responsedto.BoardCreateResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    BoardCreateResponseDTO createAddBoard(AddBoardCreateRequestDTO addBoardCreateRequestDTO,
                                          List<MultipartFile> files);
    BoardCreateResponseDTO createEctBoard(EctBoardCreateRequestDTO ectBoardCreateRequestDTO,
                                          List<MultipartFile> files);
}
