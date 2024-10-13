package com.sscanner.team.user.requestDto;

import jakarta.validation.constraints.NotBlank;

public record UserPhoneUpdateRequestDto(
        @NotBlank(message = "새 핸드폰 번호를 입력해주세요.")
        String newPhone
) {}
