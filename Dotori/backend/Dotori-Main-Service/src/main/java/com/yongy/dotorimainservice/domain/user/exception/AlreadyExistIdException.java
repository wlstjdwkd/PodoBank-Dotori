package com.yongy.dotorimainservice.domain.user.exception;

public class AlreadyExistIdException extends RuntimeException{
    public AlreadyExistIdException(String message){
        super(message);
    }
}
