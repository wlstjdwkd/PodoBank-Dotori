package com.yongy.dotori.global.security.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    NOT_MATCHING_PASSWORD(400, "비밀번호가 일치하지 않습니다."),
    NOT_MATCHING_USER(400, "사용자가 일치하지 않습니다."),
    NOT_FOUND_USER(400, "사용자가 존재하지 않습니다."),
    NOT_FOUND_REFRESH_TOKEN(400, "다시 로그인 해주세요."),
    NEW_ACCESS_TOKEN(400, "새로운 AccessToken을 발급했습니다."),
    EXPIRATION_ACCESS_TOKEN(400, "AccessToken이 만료되었습니다."),
    NOT_OURS_TOKEN(400, "우리가 발급한 토큰이 아닙니다.");

    int code;
    String message;

    ErrorType(int code , String message){
        this.code = code;
        this.message = message;
    }
}
