package com.sscanner.team.comment.service;

import com.sscanner.team.user.entity.User;
import com.sscanner.team.board.service.BoardService;
import com.sscanner.team.comment.entity.Comment;
import com.sscanner.team.comment.repository.CommentRepository;
import com.sscanner.team.comment.requestdto.CommentCreateRequestDTO;
import com.sscanner.team.comment.responsedto.CommentResponseDTO;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sscanner.team.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserUtils userUtils;
    private final BoardService boardService;

    @Override
    @Transactional
    public CommentResponseDTO saveComment(CommentCreateRequestDTO commentCreateRequestDTO) {
        boardService.getBoard(commentCreateRequestDTO.boardId());

        User user = userUtils.getUser();

        Comment comment = commentCreateRequestDTO.toEntityComment(user);

        commentRepository.save(comment);

        return CommentResponseDTO.of(comment, user);
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        Comment comment = getComment(commentId);
        commentRepository.delete(comment);
    }

    private Comment getComment(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new BadRequestException(NOT_EXIST_COMMENT));
    }
}
