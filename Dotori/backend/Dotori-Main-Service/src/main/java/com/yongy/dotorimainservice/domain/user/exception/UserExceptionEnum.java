package com.yongy.dotorimainservice.domain.user.exception;

import org.springframework.http.HttpStatus;

public enum UserExceptionEnum {

    ALREADY_EXIST_ID(HttpStatus.BAD_REQUEST,4001,"이미 존재하는 사용자입니다."), // 409

    EXPIRED_AUTHCODE(HttpStatus.BAD_REQUEST, 4002, "인증번호가 만료되었습니다."), // 404

    INVALID_AUTHCODE(HttpStatus.BAD_REQUEST, 4003, "인증번호가 올바르지 않습니다."), // 404

    ERROR_DOTORI_SIGNUP(HttpStatus.BAD_REQUEST, 4004, "회원가입에 실패했습니다."), // 400

    INVALID_DOTORI_ID(HttpStatus.NOT_FOUND, 4005, "아이디를 확인해주세요."), // 404

    INVALID_DOTORI_PWD(HttpStatus.NOT_FOUND, 4006, "비밀번호를 확인해주세요."), // 404

    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, 4007, "다시 로그인 해주세요."), // 404

    NOT_UPDATE_SOCIAL_LOGIN(HttpStatus.BAD_REQUEST, 4008, "네이버, 카카오 로그인은 비밀번호를 변경할 수 없습니다."), // 400



    NAVER_AUTH_FAIL(HttpStatus.NOT_FOUND, 5001, "네이버 인증에 실패했습니다."), // 404

    KAKAO_AUTH_FAIL(HttpStatus.NOT_FOUND, 5002, "네이버 인증에 실패했습니다."); // 404





    private HttpStatus status;
    private int code;
    private String description;

    private UserExceptionEnum(HttpStatus status, int code, String description){
        this.code=code;
        this.status=status;
        this.description=description;
    }
}
