package com.sscanner.team.user.requestdto;

import jakarta.validation.constraints.NotNull;

public record UserResetPasswordRequestDto(

        @NotNull(message = "아이디를 입력해주세요.")
        String email,
        @NotNull(message = "휴대폰 번호를 입력해주세요.")
        String phone,
        @NotNull(message = "인증번호를 입력해주세요.")
        String code,
        @NotNull(message = "비밀번호를 입력해주세요.")
        String newPassword
){
}
