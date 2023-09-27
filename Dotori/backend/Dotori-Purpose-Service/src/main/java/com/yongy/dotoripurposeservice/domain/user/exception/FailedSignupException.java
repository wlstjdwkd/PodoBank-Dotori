package com.yongy.dotoripurposeservice.domain.user.exception;

public class FailedSignupException extends RuntimeException{
    public FailedSignupException(String message){
        super(message);
    }
}
