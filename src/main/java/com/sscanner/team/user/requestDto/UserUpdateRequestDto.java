package com.sscanner.team.user.requestDto;

import lombok.Builder;

@Builder
public record UserUpdateRequestDto(

        String nickname,
        String phone
) {

}
