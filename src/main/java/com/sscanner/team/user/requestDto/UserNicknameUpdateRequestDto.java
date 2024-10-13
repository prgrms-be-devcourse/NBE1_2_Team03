package com.sscanner.team.user.requestDto;

import jakarta.validation.constraints.NotBlank;

public record UserNicknameUpdateRequestDto(
        @NotBlank(message = "새 닉네임을 입력해주세요.")
        String newNickname
){
}
