package com.yongy.dotori.domain.user.exception;

public class ExpiredAuthCodeException extends RuntimeException{
    public ExpiredAuthCodeException(String message){
        super(message);
    }
}
