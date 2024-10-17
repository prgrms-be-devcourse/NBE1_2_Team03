package com.sscanner.team.user.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshResponseDto {
    private String accessToken;
    private String refreshToken;

    public static RefreshResponseDto from(String accessToken, String refreshToken) {
        return new RefreshResponseDto(accessToken, refreshToken);
    }
}