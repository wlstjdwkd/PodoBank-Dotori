package com.yongy.dotori.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmailReqDto {
    private String id;
    private String code;

    @Builder
    public UserEmailReqDto(String id, String code) {
        this.id = id;
        this.code = code;
    }
}
