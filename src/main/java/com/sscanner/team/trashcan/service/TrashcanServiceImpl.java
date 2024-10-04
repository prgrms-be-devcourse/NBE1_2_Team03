package com.sscanner.team.trashcan.service;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.entity.TrashcanImg;
import com.sscanner.team.trashcan.repository.TrashcanRepository;
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.requestDto.UpdateTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;
import com.sscanner.team.trashcan.responseDto.TrashcanWithImgResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.sscanner.team.global.exception.ExceptionCode.NOT_EXIST_TRASHCAN_ID;

@RequiredArgsConstructor
@Service
public class TrashcanServiceImpl implements TrashcanService {

    private final TrashcanRepository trashcanRepository;
    private final TrashcanImgService trashcanImgService;


    @Override
    @Transactional
    public TrashcanWithImgResponseDto registerTrashcan(RegisterTrashcanRequestDto requestDto, MultipartFile file) {

        Trashcan trashcan = requestDto.toEntity();
        trashcanRepository.save(trashcan);

        String imgUrl = trashcanImgService.uploadTrashcanImg(file);

        TrashcanImg trashcanImg = trashcanImgService.saveTrashcanImg(trashcan.getId(), imgUrl);

        return TrashcanWithImgResponseDto.of(trashcan, trashcanImg);
    }



    @Override
    public TrashcanWithImgResponseDto getTrashcanInfo(Long trashcanId) {

        Trashcan trashcan = getTrashcanById(trashcanId);
        TrashcanImg trashcanImg = trashcanImgService.getTrashcanImg(trashcanId);

        return TrashcanWithImgResponseDto.of(trashcan, trashcanImg);
    }

    private Trashcan getTrashcanById(Long trashcanId) {
        return trashcanRepository.findById(trashcanId)
                .orElseThrow(() -> new BadRequestException(NOT_EXIST_TRASHCAN_ID));
    }

    @Override
    public TrashcanResponseDto getNearByTrashcans() {
        return null;
    }

    @Override
    public TrashcanResponseDto getNearByTrashcans2() {
        return null;
    }

    @Override
    public TrashcanResponseDto getTrashcanByRoadAddress() {
        return null;
    }


    @Transactional
    public void deleteTrashcanInfo(Long trashcanId) {

        try {
            trashcanRepository.deleteById(trashcanId);
        } catch (IllegalArgumentException e){
            throw new BadRequestException(NOT_EXIST_TRASHCAN_ID);
        }
    }

    @Transactional
    public TrashcanResponseDto updateTrashcanInfo(Long trashcanId, UpdateTrashcanRequestDto requestDto) {
        Trashcan trashcan = getTrashcanById(trashcanId);
        trashcan.updateInfo(requestDto);
        return TrashcanResponseDto.from(trashcan);
    }


}
