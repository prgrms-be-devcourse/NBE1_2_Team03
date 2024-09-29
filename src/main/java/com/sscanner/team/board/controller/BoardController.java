package com.sscanner.team.board.controller;

import com.sscanner.team.board.requestdto.AddBoardCreateRequestDTO;
import com.sscanner.team.board.requestdto.EctBoardCreateRequestDTO;
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

    @PostMapping("/add")
    public ApiResponse<BoardCreateResponseDTO> createAddBoard(@Valid @RequestPart(value = "data") AddBoardCreateRequestDTO addBoardCreateRequestDTO,
                                                              @RequestPart(value = "files") List<MultipartFile> files) {
        BoardCreateResponseDTO addBoard = boardService.createAddBoard(addBoardCreateRequestDTO, files);

        return ApiResponse.ok(201, addBoard, "쓰레기통 신고 게시글 저장 완료!!");
    }

    @PostMapping("/ect")
    public ApiResponse<BoardCreateResponseDTO> createEctBoard(@Valid @RequestPart(value = "data") EctBoardCreateRequestDTO ectBoardCreateRequestDTO,
                                                              @RequestPart(value = "files") List<MultipartFile> files) {
        BoardCreateResponseDTO ectBoard = boardService.createEctBoard(ectBoardCreateRequestDTO, files);

        return ApiResponse.ok(201, ectBoard, "쓰레기통 신고 게시글 저장 완료!!");
    }
}
