package com.sscanner.team.comment.controller;

import com.sscanner.team.comment.requestdto.CommentCreateRequestDTO;
import com.sscanner.team.comment.responsedto.CommentResponseDTO;
import com.sscanner.team.comment.service.CommentService;
import com.sscanner.team.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ApiResponse<CommentResponseDTO> createComment(@RequestBody CommentCreateRequestDTO commentCreateRequestDTO) {
        CommentResponseDTO commentInfo = commentService.saveComment(commentCreateRequestDTO);

        return ApiResponse.ok(201, commentInfo, "댓글 생성 완료!!");
    }

    @DeleteMapping("/{commmentId}")
    public ApiResponse<?> deleteComment(@PathVariable Long commmentId) {
        commentService.deleteComment(commmentId);

        return ApiResponse.ok(200, "댓글 삭제 완료!!");
    }
}