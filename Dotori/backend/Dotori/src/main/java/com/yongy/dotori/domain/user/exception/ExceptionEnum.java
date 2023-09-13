package com.yongy.dotori.domain.user.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionEnum {



    ALREADY_EXIST_ID(HttpStatus.BAD_REQUEST,4001,"이미 존재하는 아이디입니다."),

    EXPIRED_AUTHCODE(HttpStatus.BAD_REQUEST, 4002, "인증번호가 만료되었습니다."),

    INVALID_AUTHCODE(HttpStatus.BAD_REQUEST, 4002, "인증번호가 올바르지 않습니다.");


    private HttpStatus status;
    private int code;
    private String description;

    private ExceptionEnum(HttpStatus status, int code, String description){
        this.code=code;
        this.status=status;
        this.description=description;
    }
}
