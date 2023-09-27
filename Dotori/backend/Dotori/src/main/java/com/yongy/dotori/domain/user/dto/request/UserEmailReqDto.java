package com.yongy.dotori.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserEmailReqDto {
    private String id;
    private String code;

    public UserEmailReqDto(String id, String code) {
        this.id = id;
        this.code = code;
    }
}
