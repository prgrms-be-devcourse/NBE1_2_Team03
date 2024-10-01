package com.sscanner.team.board.service;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.repository.BoardRepository;
import com.sscanner.team.board.requestdto.BoardCreateRequestDTO;
import com.sscanner.team.board.responsedto.BoardCreateResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final BoardImgService boardImgService;

    /**
     * 추가, 수정, 삭제 신고 게시글 등록
     * @param boardCreateRequestDTO - Body
     * @param files - 이미지 파일들
     * @return BoardCreateResponseDTO - 신고 게시글 관련 정보
     */
    @Transactional
    @Override
    public BoardCreateResponseDTO createBoard(BoardCreateRequestDTO boardCreateRequestDTO,
                                                 List<MultipartFile> files) {
        // null에 user 들어갈 예정
        Board addBoard = boardCreateRequestDTO.toEntityAddBoard(null);

        Board savedAddBoard = boardRepository.save(addBoard);

        List<BoardImg> boardImgs = boardImgService.saveBoardImg(savedAddBoard, files);
        savedAddBoard.addBoardImgs(boardImgs);

        return BoardCreateResponseDTO.from(savedAddBoard);
    }

    @Transactional
    @Override
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }
}
