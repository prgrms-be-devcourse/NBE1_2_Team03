package com.sscanner.team.user.requestdto;

public record UserResetPasswordRequestDto(
        String email,
        String phone,
        String code,
        String newPassword
){
}
