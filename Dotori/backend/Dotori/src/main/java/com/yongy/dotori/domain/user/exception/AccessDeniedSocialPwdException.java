package com.yongy.dotori.domain.user.exception;

public class AccessDeniedSocialPwdException extends RuntimeException{
    public AccessDeniedSocialPwdException(String message){
        super(message);
    }
}
