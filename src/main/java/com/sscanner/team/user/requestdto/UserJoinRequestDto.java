package com.sscanner.team.user.requestdto;

import com.sscanner.team.user.entity.User;
import jakarta.validation.constraints.NotBlank;

public record UserJoinRequestDto(

        @NotBlank(message = "이메일이 비어있습니다.")
        String email,

        @NotBlank(message = "비밀번호가 비어있습니다.")
        String password,

        @NotBlank(message = "비밀번호 확인이 비어있습니다.")
        String passwordCheck,

        @NotBlank(message = "닉네임이 비어있습니다.")
        String nickname,

        @NotBlank(message = "전화번호가 비어있습니다.")
        String phone,

        @NotBlank(message = "인증번호가 비어있습니다.")
        String smsCode


) {


    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .phone(phone)
                .authority("ROLE_USER")
                .build();
    }



}
