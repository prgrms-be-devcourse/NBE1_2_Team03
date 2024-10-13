package com.sscanner.team.user.responsedto;

import com.sscanner.team.User;

public record UserNicknameUpdateResponseDto(
        String userId,
        String newNickname
) {
    public static UserNicknameUpdateResponseDto from(User user) {
        return new UserNicknameUpdateResponseDto(
                user.getUserId(),
                user.getNickname()
        );
    }
}
