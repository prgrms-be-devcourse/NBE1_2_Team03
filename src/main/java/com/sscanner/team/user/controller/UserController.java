package com.sscanner.team.user.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.jwt.JWTUtil;
import com.sscanner.team.user.requestDto.UserJoinRequestDto;
import com.sscanner.team.user.requestDto.UserUpdateRequestDto;
import com.sscanner.team.user.responseDto.UserJoinResponseDto;
import com.sscanner.team.user.responseDto.UserMypageResponseDto;
import com.sscanner.team.user.responseDto.UserUpdateResponseDto;
import com.sscanner.team.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;


    // 회원가입 기능
    @PostMapping("/join")
    public ApiResponse<UserJoinResponseDto> registerUSer(@RequestBody @Valid UserJoinRequestDto requestDTO) {

        // 회원가입
         userService.join(requestDTO);

        //성공 응답
        return ApiResponse.ok(201, null, "회원가입 성공");

    }

    // 마이페이지
    @GetMapping("/my-page")
    public ApiResponse<UserMypageResponseDto> getMypage(){
        return userService.getMypage();
    }

    // 회원정보 수정 (닉네임/폰 번호)
    @PutMapping("/update")
    public ApiResponse<UserUpdateResponseDto> updateUser(@RequestBody @Valid UserUpdateRequestDto requestDTO) {
        UserUpdateResponseDto responseDto = userService.updateUserInfo(requestDTO);
        return ApiResponse.ok(200, responseDto, "회원정보 수정 성공");
    }




}


