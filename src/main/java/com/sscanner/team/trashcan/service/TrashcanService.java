package com.sscanner.team.trashcan.service;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.repository.TrashcanRepository;
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static com.sscanner.team.global.exception.ExceptionCode.NOT_FOUND_TRASHCAN_ID;

@RequiredArgsConstructor
@Service
public class TrashcanService {

    private final TrashcanRepository trashcanRepository;


    @Transactional
    public TrashcanResponseDto registerTrashcan(RegisterTrashcanRequestDto requestDto) {

        Trashcan trashcan = requestDto.toEntity();

        trashcanRepository.save(trashcan);

        return TrashcanResponseDto.from(trashcan);
    }

    public TrashcanResponseDto getTrashcanById(Long trashcanId) {

        Trashcan trashcan = trashcanRepository.findById(trashcanId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_TRASHCAN_ID));

        return TrashcanResponseDto.from(trashcan);
    }
}
