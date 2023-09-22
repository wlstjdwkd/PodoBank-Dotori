package com.yongy.dotoriUserService.domain.user.exception;

public class FailedSocialAuthException extends RuntimeException{
    public FailedSocialAuthException(String message){
        super(message);
    }
}
