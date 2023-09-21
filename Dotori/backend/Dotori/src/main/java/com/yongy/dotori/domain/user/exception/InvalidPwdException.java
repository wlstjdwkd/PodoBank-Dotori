package com.yongy.dotori.domain.user.exception;

public class InvalidPwdException extends RuntimeException{
    public InvalidPwdException(String message){
        super(message);
    }
}
