package com.yongy.dotori.global.security.jwt;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenMessageResponseDto {
    int code;
    String message;
    String accessToken;

}
