package com.yongy.dotori.domain.user.dto.request;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class UserUpdatePasswordReqDto {
    private String beforePassword;
    private String afterPassword;

    public UserUpdatePasswordReqDto(String beforePassword, String afterPassword) {
        this.beforePassword = beforePassword;
        this.afterPassword = afterPassword;
    }
}
