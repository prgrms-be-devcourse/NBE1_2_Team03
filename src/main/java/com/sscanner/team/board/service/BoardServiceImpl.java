package com.sscanner.team.board.service;

import com.sscanner.team.User;
import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.repository.BoardRepository;
import com.sscanner.team.board.requestdto.BoardCreateRequestDTO;
import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO;
import com.sscanner.team.board.responsedto.BoardResponseDTO;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sscanner.team.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final BoardImgService boardImgService;
    private final UserRepository userRepository;

    /**
     * 추가, 수정, 삭제 신고 게시글 등록
     * @param boardCreateRequestDTO - Body
     * @param files - 이미지 파일들
     * @return BoardCreateResponseDTO - 신고 게시글 관련 정보
     */
    @Transactional
    @Override
    public BoardResponseDTO createBoard(BoardCreateRequestDTO boardCreateRequestDTO,
                                        List<MultipartFile> files) {
        User user = userRepository.findById(boardCreateRequestDTO.userId()).get();
        // null에 user 들어갈 예정
        Board addBoard = boardCreateRequestDTO.toEntityAddBoard(user);

        Board savedAddBoard = boardRepository.save(addBoard);

        List<BoardImg> boardImgs = boardImgService.saveBoardImg(savedAddBoard.getId(), files);

        return BoardResponseDTO.from(savedAddBoard, boardImgs);
    }

    /**
     * 신고 게시글 논리 삭제
     * @param boardId - 게시글 id
     */
    @Transactional
    @Override
    public void deleteBoard(Long boardId) {
        Board board = getBoard(boardId);

        boardImgService.deleteBoardImgs(board.getId());

        boardRepository.delete(board);
    }

    /**
     * 게시글 수정
     * @param boardId - 게시글 id
     * @param boardUpdateRequestDTO - Body
     * @param files - 변경된 이미지 파일들
     * @return BoardResponseDTO
     */
    @Transactional
    @Override
    public BoardResponseDTO updateBoard(Long boardId,
                                           BoardUpdateRequestDTO boardUpdateRequestDTO,
                                           List<MultipartFile> files) {
        Board board = getBoard(boardId);

        board.updateBoardInfo(boardUpdateRequestDTO);

        List<BoardImg> boardImgs;
        if(files.get(0).isEmpty()) {
            boardImgs = boardImgService.getBoardImgs(boardId);
        } else {
            boardImgs = boardImgService.updateBoardImgs(board.getId(), files);
        }

        return BoardResponseDTO.from(board, boardImgs);
    }

    @Override
    public Board getBoard(Long boardId) {
        return boardRepository
                .findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_EXIST_BOARD));
    }

}
