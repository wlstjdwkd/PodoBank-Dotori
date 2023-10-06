package com.yongy.dotori.domain.user.dto.request;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class UserRefreshTokenDto {
    private String refreshToken;

    public UserRefreshTokenDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
