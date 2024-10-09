package com.sscanner.team.user.requestdto;

import jakarta.validation.constraints.NotEmpty;

public record SmsRequestDto(
        @NotEmpty(message = "휴대폰 번호를 입력해주세요")
        String phoneNum
) {

}