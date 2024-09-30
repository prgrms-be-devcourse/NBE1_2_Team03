package com.sscanner.team.trashcan.service;

import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.requestDto.UpdateTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;

public interface TrashService {

    TrashcanResponseDto registerTrashcan(RegisterTrashcanRequestDto requestDto);
    TrashcanResponseDto getTrashcanInfo(Long trashcanId);
    TrashcanResponseDto getNearByTrashcans();
    TrashcanResponseDto getNearByTrashcans2();
    TrashcanResponseDto getTrashcanByRoadAddress();
    TrashcanResponseDto updateTrashcanInfo(Long trashcanId, UpdateTrashcanRequestDto requestDto);
    void deleteTrashcanInfo(Long trashcanId);
}
