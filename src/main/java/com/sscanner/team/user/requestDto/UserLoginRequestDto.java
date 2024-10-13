package com.sscanner.team.user.requestDto;

public record UserLoginRequestDto(
        String email,
        String password
) {
}
