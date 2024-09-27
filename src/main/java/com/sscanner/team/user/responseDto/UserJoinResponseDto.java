package com.sscanner.team.user.responseDto;

import com.sscanner.team.User;

public record UserJoinResponseDto(
        String userId,
        String email,
        String nickname,
        String phone
){

    public static UserJoinResponseDto fromEntity(User user) {
        return new UserJoinResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getPhone()
        );
    }
}