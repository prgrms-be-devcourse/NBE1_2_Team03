package com.sscanner.team.points.responsedto;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.user.entity.User;

public record PointResponseDto(
        String userPointId,
        User user,
        Integer point
) {
    public static PointResponseDto from(UserPoint userPoint) {
        return new PointResponseDto(
                userPoint.getId(),
                userPoint.getUser(),
                userPoint.getPoint()
        );
    }

    public UserPoint toEntity() {
        return UserPoint.builder()
                .id(userPointId)
                .user(user)
                .point(point)
                .build();
    }
}
