package com.sscanner.team.user.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.DuplicateException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.user.requestDto.UserJoinRequestDto;
import com.sscanner.team.user.responseDto.UserJoinResponseDto;
import com.sscanner.team.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    // 회원가입 기능
    @PostMapping("/join")
    public ApiResponse<UserJoinResponseDto> registerUSer(@RequestBody @Valid UserJoinRequestDto requestDTO) {

        // 회원가입
         userService.join(requestDTO);

        //성공 응답
        return ApiResponse.ok(201, null, "회원가입 성공");

    }
}

