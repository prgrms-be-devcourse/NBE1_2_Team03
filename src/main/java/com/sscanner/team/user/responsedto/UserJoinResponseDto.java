package com.sscanner.team.user.responsedto;

import com.sscanner.team.User;

public record UserJoinResponseDto(
        String userId,
        String email,
        String nickname,
        String phone
){

    public static UserJoinResponseDto from(User user) {
        return new UserJoinResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getPhone()
        );
    }
}