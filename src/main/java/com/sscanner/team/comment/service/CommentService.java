package com.sscanner.team.comment.service;

import com.sscanner.team.comment.requestdto.CommentCreateRequestDTO;
import com.sscanner.team.comment.responsedto.CommentResponseDTO;

public interface CommentService {
    CommentResponseDTO saveComment(CommentCreateRequestDTO commentCreateRequestDTO);
    void deleteComment(Long commentId);
}
