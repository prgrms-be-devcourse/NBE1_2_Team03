package com.sscanner.team.board.controller;

import com.sscanner.team.board.requestdto.BoardCreateRequestDTO;
import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO;
import com.sscanner.team.board.responsedto.BoardResponseDTO;
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
    public ApiResponse<BoardResponseDTO> createAddBoard(@Valid @RequestPart(value = "data") BoardCreateRequestDTO boardCreateRequestDTO,
                                                        @RequestPart(value = "files") List<MultipartFile> files) {
        BoardResponseDTO board = boardService.createBoard(boardCreateRequestDTO, files);

        return ApiResponse.ok(201, board, "쓰레기통 신고 게시글 저장 완료!!");
    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<?> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);

        return ApiResponse.ok(200, "게시판 삭제 완료!!");
    }

    @PatchMapping("/{boardId}")
    public ApiResponse<BoardResponseDTO> updateBoard(@PathVariable Long boardId,
                                     @RequestPart(value = "data") BoardUpdateRequestDTO boardUpdateRequestDTO,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        BoardResponseDTO board = boardService.updateBoard(boardId, boardUpdateRequestDTO, files);

        return ApiResponse.ok(200, board, "게시판 수정 완료!!");
    }
}