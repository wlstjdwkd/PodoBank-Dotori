package com.yongy.dotoripurposeservice.domain.user.exception;

public class AccessDeniedSocialPwdException extends RuntimeException{
    public AccessDeniedSocialPwdException(String message){
        super(message);
    }
}
