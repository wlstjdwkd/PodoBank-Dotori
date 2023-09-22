package com.yongy.dotoriUserService.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginReqDto {
    private String id;
    private String password;

    @Builder
    public UserLoginReqDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
