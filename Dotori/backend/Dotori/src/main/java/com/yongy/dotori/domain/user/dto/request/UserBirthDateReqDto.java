package com.yongy.dotori.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBirthDateReqDto {
    private String birthDate;

    @Builder
    public UserBirthDateReqDto(String birthDate) {
        this.birthDate = birthDate;
    }
}
