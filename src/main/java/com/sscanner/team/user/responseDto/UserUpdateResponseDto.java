package com.sscanner.team.user.responseDto;

import com.sscanner.team.User;
import lombok.Builder;


@Builder
public record UserUpdateResponseDto (
        String email,
        String nickname,
        String phone
){
    public static UserUpdateResponseDto from(User user) {
        return UserUpdateResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .build();
    }
}
