package com.sscanner.team.board.controller;

import com.sscanner.team.board.requestdto.BoardCreateRequestDTO;
import com.sscanner.team.board.responsedto.BoardCreateResponseDTO;
import com.sscanner.team.board.service.BoardService;
import com.sscanner.team.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping()
    public ApiResponse<BoardCreateResponseDTO> createAddBoard(@Valid @RequestPart(value = "data") BoardCreateRequestDTO boardCreateRequestDTO,
                                                              @RequestPart(value = "files") List<MultipartFile> files) {
        BoardCreateResponseDTO board = boardService.createBoard(boardCreateRequestDTO, files);

        return ApiResponse.ok(201, board, "쓰레기통 신고 게시글 저장 완료!!");
    }
}
