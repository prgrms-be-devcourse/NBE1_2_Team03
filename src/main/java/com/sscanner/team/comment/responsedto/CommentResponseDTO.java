package com.sscanner.team.comment.responsedto;

import com.sscanner.team.user.entity.User;
import com.sscanner.team.comment.entity.Comment;

public record CommentResponseDTO(
        Long id,
        String nickname,
        String authority,
        String content
) {
    public static CommentResponseDTO of(Comment comment, User user) {
        return new CommentResponseDTO(
                comment.getId(),
                user.getNickname(),
                user.getAuthority(),
                comment.getContent()
        );
    }
}
