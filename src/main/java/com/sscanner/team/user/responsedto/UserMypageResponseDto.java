package com.sscanner.team.user.responsedto;

import com.sscanner.team.user.entity.User;
import lombok.Builder;

@Builder
public record UserMypageResponseDto(
        String email,
        String nickname,
        String phone
) {
    public static UserMypageResponseDto create(User user) {
        return new UserMypageResponseDto(user.getEmail(), user.getNickname(), user.getPhone());
    }
}