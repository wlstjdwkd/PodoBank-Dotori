package com.yongy.dotori.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRefreshTokenDto {
    private String refreshToken;

    @Builder
    public UserRefreshTokenDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
