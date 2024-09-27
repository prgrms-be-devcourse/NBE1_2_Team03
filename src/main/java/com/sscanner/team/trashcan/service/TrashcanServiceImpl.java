package com.sscanner.team.trashcan.service;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.repository.TrashcanRepository;
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sscanner.team.global.exception.ExceptionCode.NOT_EXIST_TRASHCAN_ID;

@RequiredArgsConstructor
@Service
public class TrashcanServiceImpl implements TrashService{

    private final TrashcanRepository trashcanRepository;

    @Override
    @Transactional
    public TrashcanResponseDto registerTrashcan(RegisterTrashcanRequestDto requestDto) {

        Trashcan trashcan = requestDto.toEntity();

        trashcanRepository.save(trashcan);

        return TrashcanResponseDto.from(trashcan);
    }

    @Override
    public TrashcanResponseDto getTrashcanInfoById(Long trashcanId) {

        Trashcan trashcan = trashcanRepository.findById(trashcanId)
                .orElseThrow(() -> new BadRequestException(NOT_EXIST_TRASHCAN_ID));

        return TrashcanResponseDto.from(trashcan);
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

    @Override
    public TrashcanResponseDto updateTrashcan() {
        return null;
    }

    @Override
    public TrashcanResponseDto deleteTrashcan() {
        return null;
    }


}
