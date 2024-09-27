package com.sscanner.team.user;

import com.sscanner.team.User;

public record UserJoinResponseDTO (
        String userId,
        String email,
        String nickname,
        String phone
){

    public static UserJoinResponseDTO fromEntity(User user) {
        return new UserJoinResponseDTO(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getPhone()
        );
    }
}