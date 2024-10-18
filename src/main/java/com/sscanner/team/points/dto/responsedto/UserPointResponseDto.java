package com.sscanner.team.points.dto.responsedto;

public record UserPointResponseDto(
        String userId,
        Integer point
) {
    public static UserPointResponseDto of(String userId, Integer point) {
        return new UserPointResponseDto(userId, point);
    }
}
