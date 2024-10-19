package com.sscanner.team.user.requestdto;

import jakarta.validation.constraints.NotBlank;

public record UserPhoneUpdateRequestDto(
        @NotBlank(message = "새 핸드폰 번호를 입력해주세요.")
        String newPhone,

        @NotBlank(message = "인증번호가 비어있습니다.")
        String smsCode
) {}
