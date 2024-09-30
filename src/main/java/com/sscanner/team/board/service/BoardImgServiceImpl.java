package com.sscanner.team.board.service;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.repository.BoardImgRepository;
import com.sscanner.team.global.common.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardImgServiceImpl implements BoardImgService{

    private final BoardImgRepository boardImgRepository;
    private final ImageService imageService;

    @Override
    public List<BoardImg> saveBoardImg(Board board, List<MultipartFile> files) {
        List<BoardImg> boardImgs = new ArrayList<>();
        for(MultipartFile file : files) {
            String boardImgUrl = imageService.makeImgUrl(file);

            BoardImg boardImg = BoardImg.makeBoardImg(board, boardImgUrl);

            boardImgs.add(boardImg);
        }

        return boardImgRepository.saveAll(boardImgs);
    }
}
