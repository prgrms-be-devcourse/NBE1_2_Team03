package com.sscanner.team.user.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.user.responseDto.RefreshResponseDto;
import com.sscanner.team.user.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ApiResponse<RefreshResponseDto> reissue(HttpServletRequest request, HttpServletResponse response) {
        reissueService.reissueToken(request, response);
        return ApiResponse.ok(201, null, "토큰 재발급 성공");
    }
}

