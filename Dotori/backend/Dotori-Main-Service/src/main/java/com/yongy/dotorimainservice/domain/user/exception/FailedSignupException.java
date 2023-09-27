package com.yongy.dotorimainservice.domain.user.exception;

public class FailedSignupException extends RuntimeException{
    public FailedSignupException(String message){
        super(message);
    }
}
