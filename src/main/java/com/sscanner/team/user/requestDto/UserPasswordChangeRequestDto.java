package com.sscanner.team.user.requestDto;

import lombok.Builder;

@Builder
public record UserPasswordChangeRequestDto(
        String currentPassword,
        String newPassword,
        String confirmNewPassword
) {
}