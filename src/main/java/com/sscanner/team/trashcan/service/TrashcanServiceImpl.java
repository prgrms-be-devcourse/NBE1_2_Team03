package com.sscanner.team.trashcan.service;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.repository.TrashcanRepository;
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.sscanner.team.global.exception.ExceptionCode.NOT_EXIST_TRASHCAN_ID;
import static com.sscanner.team.global.exception.ExceptionCode.NO_NEARBY_TRASHCAN_FOUND;

@RequiredArgsConstructor
@Service
public class TrashcanServiceImpl implements TrashService{

    //쓰레기통 위도, 경도 검색 범위 설정 미터단위
    private static final int RADIUS_FOR_SEARCH = 200;

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

    //좌표로 근처 쓰레기통을 조회하는 메서드
    @Override
    public List<TrashcanResponseDto> getTrashcansByCoordinate(BigDecimal latitude, BigDecimal longitude) {

        List<Trashcan> trashcans = getTrashcansByLatAndLongitude(latitude, longitude);

        return convertToTrashcanResponses(trashcans);
    }

    private List<Trashcan> getTrashcansByLatAndLongitude(BigDecimal latitude, BigDecimal longitude) {
        return trashcanRepository.findTrashcansWithinRadius(latitude, longitude, RADIUS_FOR_SEARCH)
                .orElseThrow(() -> new BadRequestException(NO_NEARBY_TRASHCAN_FOUND));
    }

    private static List<TrashcanResponseDto> convertToTrashcanResponses(List<Trashcan> trashcans) {
        List<TrashcanResponseDto> trashcanResponseDtos = new ArrayList<>();
        for (Trashcan trashcan : trashcans) {
            trashcanResponseDtos.add(TrashcanResponseDto.from(trashcan));
        }
        return trashcanResponseDtos;
    }

    @Override
    public TrashcanResponseDto getTrashcanByRoadAddress() {
        return null;
    }

    @Override
    public TrashcanResponseDto getNearByTrashcans2() {
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
