package com.sscanner.team.trashcan.service;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.repository.TrashcanRepository;
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.requestDto.UpdateTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sscanner.team.global.exception.ExceptionCode.NOT_EXIST_TRASHCAN_ID;

@RequiredArgsConstructor
@Service
public class TrashcanServiceImpl implements TrashcanService {

    private final TrashcanRepository trashcanRepository;

    @Override
    @Transactional
    public TrashcanResponseDto registerTrashcan(RegisterTrashcanRequestDto requestDto) {

        Trashcan trashcan = requestDto.toEntity();

        trashcanRepository.save(trashcan);

        return TrashcanResponseDto.from(trashcan);
    }

    @Override
    public TrashcanResponseDto getTrashcanInfo(Long trashcanId) {

        Trashcan trashcan = getTrashcanById(trashcanId);

        return TrashcanResponseDto.from(trashcan);
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
        Trashcan trashcan = getTrashcanById(trashcanId);
        trashcanRepository.delete(trashcan);
    }

    @Transactional
    public TrashcanResponseDto updateTrashcanInfo(Long trashcanId, UpdateTrashcanRequestDto requestDto) {
        Trashcan trashcan = getTrashcanById(trashcanId);
        trashcan.updateInfo(requestDto);
        return TrashcanResponseDto.from(trashcan);
    }


}
