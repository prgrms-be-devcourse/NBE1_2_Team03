package com.sscanner.team.points.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.points.responsedto.PointResponseDto;
import com.sscanner.team.points.service.PaymentServiceImpl;
import com.sscanner.team.points.service.PointServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointController {

    private final PointServiceImpl pointService;
    private final PaymentServiceImpl paymentService;

    /**
     * 사용자의 현재 포인트를 조회하는 API
     * @param userId 사용자 ID
     * @return 포인트 조회 성공 메시지
     */
    @GetMapping("/{userId}")
    public ApiResponse<PointResponseDto> getUserPoints(@PathVariable String userId) {
        Integer points = pointService.getPoint(userId);

        PointResponseDto pointResponseDto = new PointResponseDto(userId, points);

        return ApiResponse.ok(200, pointResponseDto, "사용자 포인트 조회 성공");
    }

    /**
     * 사용자의 포인트를 추가하는 API
     * @param userId 사용자 ID
     * @param points 추가할 포인트
     * @return 포인트 추가 성공 메시지
     */
    @PostMapping("/{userId}/add")
    public ApiResponse<PointResponseDto> addUserPoints(
            @PathVariable String userId,
            @RequestParam Integer points) {

        PointResponseDto response = pointService.addPoint(userId, points);

        return ApiResponse.ok(201, response, "포인트가 성공적으로 추가되었습니다.");
    }

    /**
     * 사용자의 포인트를 사용하는 API
     * @param userId 사용자 ID
     * @param productId 상품 ID
     * @return 포인트 사용 성공 메시지
     */
    @PostMapping("/{userId}/pay/{productId}")
    public ApiResponse<PointResponseDto> payUserPoints(
            @PathVariable String userId,
            @PathVariable Long productId) {

        PointResponseDto response = paymentService.payPoint(userId, productId);

        return ApiResponse.ok(201, response, "포인트가 성공적으로 사용되었습니다.");
    }
}
