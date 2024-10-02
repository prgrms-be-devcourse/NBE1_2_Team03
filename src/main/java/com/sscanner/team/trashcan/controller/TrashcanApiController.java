package com.sscanner.team.trashcan.controller;


import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.requestDto.UpdateTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;
import com.sscanner.team.trashcan.responseDto.TrashcanWithImgResponseDto;
import com.sscanner.team.trashcan.service.TrashcanImgService;
import com.sscanner.team.trashcan.service.TrashcanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/trashcan")
public class TrashcanApiController {

    private final TrashcanService trashcanService;

    @PostMapping()
    public ApiResponse<TrashcanWithImgResponseDto> registerTrashcan(@RequestPart(value = "data") @Valid RegisterTrashcanRequestDto requestDto,
                                                                     @RequestPart(value = "file") MultipartFile file){

        TrashcanWithImgResponseDto responseDto = trashcanService.registerTrashcan(requestDto, file);

        return ApiResponse.ok(201, responseDto, "쓰레기통 등록 성공");
    }

    @GetMapping("/{trashcanId}")
    public ApiResponse<TrashcanWithImgResponseDto> getTrashcanInfo(@PathVariable Long trashcanId){

        TrashcanWithImgResponseDto responseDto = trashcanService.getTrashcanInfo(trashcanId);

        return ApiResponse.ok(200, responseDto, "쓰레기통 조회 성공");
    }

    @PutMapping("/{trashcanId}")
    public ApiResponse<TrashcanResponseDto> updateProductInfo(@RequestBody @Valid UpdateTrashcanRequestDto requestDto, @PathVariable Long trashcanId){
        TrashcanResponseDto responseDto = trashcanService.updateTrashcanInfo(trashcanId, requestDto);
        return ApiResponse.ok(200, responseDto, "쓰레기통 정보 변경 성공");
    }

    @DeleteMapping("/{trashcanId}")
    public ApiResponse<?> deleteTrashcanInfo(@PathVariable Long trashcanId){
        trashcanService.deleteTrashcanInfo(trashcanId);
        return ApiResponse.ok(200, "쓰레기통 정보 삭제 성공");
    }

}
