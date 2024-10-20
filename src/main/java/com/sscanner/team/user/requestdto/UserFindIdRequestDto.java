package com.sscanner.team.user.requestdto;

import jakarta.validation.constraints.NotNull;

public record UserFindIdRequestDto(

        @NotNull(message = "휴대폰 번호를 입력해주세요.")
        String phone,
        String code
) {
}
