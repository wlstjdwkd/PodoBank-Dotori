package com.yongy.dotoriUserService.domain.user.exception;

public class AlreadyExistIdException extends RuntimeException{
    public AlreadyExistIdException(String message){
        super(message);
    }
}
