package com.yongy.dotori.global.security.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenMsgResDto {
    int code;
    String message;
    String accessToken;

}
