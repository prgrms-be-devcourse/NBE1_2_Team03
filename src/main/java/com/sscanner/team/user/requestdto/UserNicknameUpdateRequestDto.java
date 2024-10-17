package com.sscanner.team.user.requestdto;

import com.sscanner.team.user.entity.User;
import jakarta.validation.constraints.NotBlank;


public record UserNicknameUpdateRequestDto(
        @NotBlank(message = "새 닉네임을 입력해주세요.")
        String newNickname
){ }
