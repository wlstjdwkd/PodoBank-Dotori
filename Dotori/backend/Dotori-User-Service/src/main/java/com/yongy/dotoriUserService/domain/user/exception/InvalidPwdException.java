package com.yongy.dotoriUserService.domain.user.exception;

public class InvalidPwdException extends RuntimeException{
    public InvalidPwdException(String message){
        super(message);
    }
}
