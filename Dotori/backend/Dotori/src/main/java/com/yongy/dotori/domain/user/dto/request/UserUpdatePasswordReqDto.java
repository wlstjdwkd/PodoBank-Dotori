package com.yongy.dotori.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdatePasswordReqDto {
    private String beforePassword;
    private String afterPassword;

    @Builder
    public UserUpdatePasswordReqDto(String beforePassword, String afterPassword) {
        this.beforePassword = beforePassword;
        this.afterPassword = afterPassword;
    }
}
