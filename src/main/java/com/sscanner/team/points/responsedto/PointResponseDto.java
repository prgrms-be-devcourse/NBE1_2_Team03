package com.sscanner.team.points.responsedto;

public record PointResponseDto(
        String userId,
        Integer point
) {
    public static PointResponseDto of(String userId, Integer point) {
        return new PointResponseDto(userId, point);
    }
}
