package com.sscanner.team.trashcan.service;

import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface TrashService {

    TrashcanResponseDto registerTrashcan(RegisterTrashcanRequestDto requestDto);
    TrashcanResponseDto getTrashcanInfo(Long trashcanId);
    List<TrashcanResponseDto> getTrashcansByCoordinate(BigDecimal latitude, BigDecimal longitude);

    TrashcanResponseDto getNearByTrashcans2();
    TrashcanResponseDto getTrashcanByRoadAddress();
    TrashcanResponseDto updateTrashcan();
    TrashcanResponseDto deleteTrashcan();
}
