package com.sscanner.team.board.service;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.repository.BoardRepository;
import com.sscanner.team.board.requestdto.AddBoardCreateRequestDTO;
import com.sscanner.team.board.requestdto.EctBoardCreateRequestDTO;
import com.sscanner.team.board.responsedto.BoardCreateResponseDTO;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.repository.TrashcanRepository;
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
    private final TrashcanRepository trashcanRepository;

    /**
     * 추가 신고 게시글
     * 추가 신고 게시글은 trashcan 관련 정보가 필요해서 따로 뺐습니다.
     * @param addBoardCreateRequestDTO - Body
     * @param files - 이미지 파일들
     * @return BoardCreateResponseDTO - 신고 게시글 관련 정보들
     */
    @Transactional
    @Override
    public BoardCreateResponseDTO createAddBoard(AddBoardCreateRequestDTO addBoardCreateRequestDTO,
                                                 List<MultipartFile> files) {
        Trashcan trashcan = addBoardCreateRequestDTO.toEntityTrashcan();

        Trashcan savedTrashcan = trashcanRepository.save(trashcan);
        // null에 user 들어갈 예정
        Board addBoard = addBoardCreateRequestDTO.toEntityAddBoard(null, savedTrashcan.getId());

        Board savedAddBoard = boardRepository.save(addBoard);

        List<BoardImg> boardImgs = boardImgService.saveBoardImg(savedAddBoard, files);
        savedAddBoard.addBoardImgs(boardImgs);

        return BoardCreateResponseDTO.from(savedAddBoard);
    }

    /**
     * 수정, 삭제 신고 게시글
     * 수정, 삭제 신고 게시글은 trashcanId만 필요하다.
     * @param ectBoardCreateRequestDTO - Body
     * @param files - 이미지 파일들
     * @return BoardCreateResponseDTO - 신고 게시글 관련 정보들
     */
    @Transactional
    @Override
    public BoardCreateResponseDTO createEctBoard(EctBoardCreateRequestDTO ectBoardCreateRequestDTO,
                               List<MultipartFile> files) {
        // null에 user 들어갈 예정
        Board addBoard = ectBoardCreateRequestDTO.toEntityEctBoard(null);

        Board savedAddBoard = boardRepository.save(addBoard);

        List<BoardImg> boardImgs = boardImgService.saveBoardImg(savedAddBoard, files);
        savedAddBoard.addBoardImgs(boardImgs);

        return BoardCreateResponseDTO.from(savedAddBoard);
    }
}
