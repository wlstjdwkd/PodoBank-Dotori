package com.yongy.dotori.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {
    private String id;
    private String password;

    @Builder
    public UserLoginDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
