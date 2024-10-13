package com.sscanner.team.user.requestdto;

import lombok.Builder;

@Builder
public record UserPasswordChangeRequestDto(
        String currentPassword,
        String newPassword,
        String confirmNewPassword
) {
}