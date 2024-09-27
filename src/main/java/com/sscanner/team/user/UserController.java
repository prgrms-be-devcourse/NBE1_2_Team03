package com.sscanner.team.user;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.DuplicateException;
import com.sscanner.team.global.exception.ExceptionCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    //
    @GetMapping("/join")
    @ResponseBody
    public ApiResponse<String> registerUSer(){
        return ApiResponse.ok(200, "회원가입 API. POST 요청으로 가입 정보 전송.", "회원가입 페이지 이동");    }

    // 회원가입 기능
    @PostMapping("/join")
    public ApiResponse<UserJoinResponseDTO> registerUSer(@RequestBody @Valid UserJoinRequestDTO requestDTO) {

        // 이메일 (아이디) 중복체크
        if(userService.checkEmail(  requestDTO.email()))
            throw new DuplicateException(ExceptionCode.DUPLICATED_EMAIL);


        // 닉네임 중복 체크
        if(userService.checkNickname(  requestDTO.nickname()))
            throw new DuplicateException(ExceptionCode.DUPLICATED_NICKNAME);

        //핸드폰 중복 체크
        if(userService.checkPhone(  requestDTO.phone()))
            throw new DuplicateException(ExceptionCode.DUPLICATED_PHONE);

        // 비밀번호, 비밀번호 확인 체크
        if (!userService.confirmPassword( requestDTO.password(),  requestDTO.passwordCheck()))
            throw new BadRequestException(ExceptionCode.PASSWORD_NOT_MATCH);

        // 회원가입
        UserJoinResponseDTO responseDTO = userService.join(requestDTO);

        //성공 응답
        return ApiResponse.ok(201, null, "회원가입 성공");

    }



}
