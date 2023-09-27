package com.yongy.dotorimainservice.domain.user.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserAccessTokenDto {
    private String accessToken;
}
