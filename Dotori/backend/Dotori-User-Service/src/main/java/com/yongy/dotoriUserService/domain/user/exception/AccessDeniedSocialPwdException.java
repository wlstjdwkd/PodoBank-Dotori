package com.yongy.dotoriUserService.domain.user.exception;

public class AccessDeniedSocialPwdException extends RuntimeException{
    public AccessDeniedSocialPwdException(String message){
        super(message);
    }
}
