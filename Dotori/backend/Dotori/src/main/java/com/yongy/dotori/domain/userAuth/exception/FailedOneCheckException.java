package com.yongy.dotori.domain.userAuth.exception;

public class FailedOneCheckException extends RuntimeException{
    public FailedOneCheckException(String message){
        super(message);
    }
}
