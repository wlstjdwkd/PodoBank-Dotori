package com.yongy.dotori.domain.user.exception;

public class FailedSocialAuthException extends RuntimeException{
    public FailedSocialAuthException(String message){
        super(message);
    }
}
