package com.yongy.dotori.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserPhoneNumberReqDto {
    private String phoneNumber;

    public UserPhoneNumberReqDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
