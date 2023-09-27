package com.yongy.dotoripurposeservice.domain.user.exception;

public class ExpiredAuthCodeException extends RuntimeException{
    public ExpiredAuthCodeException(String message){
        super(message);
    }
}
