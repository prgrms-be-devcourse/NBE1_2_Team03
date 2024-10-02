package com.sscanner.team.trashcan.service;

import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.requestDto.UpdateTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;
import com.sscanner.team.trashcan.responseDto.TrashcanWithImgResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface TrashcanService {


    TrashcanWithImgResponseDto registerTrashcan(RegisterTrashcanRequestDto requestDto, MultipartFile file);
    TrashcanWithImgResponseDto getTrashcanInfo(Long trashcanId);


    TrashcanResponseDto getNearByTrashcans();
    TrashcanResponseDto getNearByTrashcans2();
    TrashcanResponseDto getTrashcanByRoadAddress();
    void deleteTrashcanInfo(Long trashcanId);

    TrashcanResponseDto updateTrashcanInfo(Long trashcanId, UpdateTrashcanRequestDto requestDto);


}
