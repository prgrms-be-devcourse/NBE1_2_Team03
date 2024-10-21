package com.sscanner.team.points.requestdto;

import com.sscanner.team.user.entity.User;
import com.sscanner.team.points.entity.UserPoint;


public record PointUpdateRequestDto(
        String userPointId,
        User user,
        Integer newPoint
) {
    public static PointUpdateRequestDto of(String userPointId, User user, Integer newPoint) {
        return new PointUpdateRequestDto(userPointId, user, newPoint);
    }

    public UserPoint toEntity() {
        return UserPoint.builder()
                .id(userPointId)
                .user(user)
                .point(newPoint)
                .build();
    }
}
