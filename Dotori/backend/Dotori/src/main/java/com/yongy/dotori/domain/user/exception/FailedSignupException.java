package com.yongy.dotori.domain.user.exception;

public class FailedSignupException extends RuntimeException{
    public FailedSignupException(String message){
        super(message);
    }
}
