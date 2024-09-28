package com.sscanner.team.trashcan.controller;


import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;
import com.sscanner.team.trashcan.service.TrashcanServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/trashcan")
public class TrashcanApiController {

    private final TrashcanServiceImpl trashcanServiceImpl;

    @PostMapping()
    public ApiResponse<TrashcanResponseDto> registerTrashcan(@RequestBody @Valid RegisterTrashcanRequestDto requestDto){

        TrashcanResponseDto responseDto = trashcanServiceImpl.registerTrashcan(requestDto);

        return ApiResponse.ok(201, responseDto, "쓰레기통 등록 성공");
    }

    @GetMapping("/{trashcanId}")
    public ApiResponse<TrashcanResponseDto> getTrashcanInfoById(@PathVariable Long trashcanId){

        TrashcanResponseDto responseDto = trashcanServiceImpl.getTrashcanInfo(trashcanId);

        return ApiResponse.ok(200, responseDto, "쓰레기통 조회 성공");
    }


    @GetMapping("/nearByCoordinate")
    public ApiResponse<List<TrashcanResponseDto>> getTrashcanNearByCoordinate(    @NotNull(message = "위도는 필수입니다.")
                                                                    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다.")
                                                                    @DecimalMax(value = "90.0", message = "위도는 90 이하이어야 합니다.")
                                                                    @RequestParam BigDecimal latitude,

                                                                    @NotNull(message = "경도는 필수입니다.")
                                                                    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다.")
                                                                    @DecimalMax(value = "180.0", message = "경도는 180 이하이어야 합니다.")
                                                                    @RequestParam BigDecimal longitude){

        List<TrashcanResponseDto> responseDtos = trashcanServiceImpl.getTrashcansByCoordinate(latitude, longitude);

        return ApiResponse.ok(200, responseDtos, "근처 쓰레기통 조회 성공");
    }



}
