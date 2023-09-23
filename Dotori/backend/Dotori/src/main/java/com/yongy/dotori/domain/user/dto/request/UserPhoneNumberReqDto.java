package com.yongy.dotori.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPhoneNumberReqDto {
    private String phoneNumber;

    @Builder
    public UserPhoneNumberReqDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
