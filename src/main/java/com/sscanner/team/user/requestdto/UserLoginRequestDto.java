package com.sscanner.team.user.requestdto;

public record UserLoginRequestDto(
        String email,
        String password
) {
}
