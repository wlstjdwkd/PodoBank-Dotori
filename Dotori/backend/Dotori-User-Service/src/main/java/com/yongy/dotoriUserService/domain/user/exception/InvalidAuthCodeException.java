package com.yongy.dotoriUserService.domain.user.exception;

public class InvalidAuthCodeException extends RuntimeException{
    public InvalidAuthCodeException(String message){
        super(message);
    }
}
