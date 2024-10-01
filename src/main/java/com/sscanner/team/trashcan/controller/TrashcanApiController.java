package com.sscanner.team.trashcan.controller;


import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.requestDto.UpdateTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;
import com.sscanner.team.trashcan.service.TrashcanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/trashcan")
public class TrashcanApiController {

    private final TrashcanService trashcanService;

    @PostMapping()
    public ApiResponse<TrashcanResponseDto> registerTrashcan(@RequestBody @Valid RegisterTrashcanRequestDto requestDto){

        TrashcanResponseDto responseDto = trashcanService.registerTrashcan(requestDto);

        return ApiResponse.ok(201, responseDto, "쓰레기통 등록 성공");
    }

    @GetMapping("/{trashcanId}")
    public ApiResponse<TrashcanResponseDto> getTrashcanInfoById(@PathVariable Long trashcanId){

        TrashcanResponseDto responseDto = trashcanService.getTrashcanInfo(trashcanId);

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
