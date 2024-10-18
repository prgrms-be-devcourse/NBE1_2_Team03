package com.sscanner.team.points.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.points.dto.requestdto.PointRequestDto;
import com.sscanner.team.points.dto.responsedto.UserPointResponseDto;
import com.sscanner.team.points.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointController {

    private final PointService pointService;

    /**
     * 사용자의 현재 포인트를 조회하는 API
     * @param userId 사용자 ID
     * @return 포인트 조회 성공 메시지
     */
    @GetMapping("/{userId}")
    public ApiResponse<UserPointResponseDto> getUserPoints(@PathVariable String userId) {
        UserPointResponseDto response = pointService.getCachedPoint(userId);
        return ApiResponse.ok(200, response, "사용자 포인트 조회 성공");
    }

    /**
     * 사용자의 포인트를 추가하는 API
     * @param pointRequestDto 사용자 ID, 추가할 포인트
     * @return 포인트 추가 성공 메시지
     */
    @PostMapping("/add")
    public ApiResponse<UserPointResponseDto> addUserPoints(@RequestBody PointRequestDto pointRequestDto) {
        UserPointResponseDto response = pointService.addPoint(pointRequestDto);
        return ApiResponse.ok(201, response, "포인트가 성공적으로 추가되었습니다.");
    }
}
