package com.yongy.dotoripurposeservice.domain.user.exception;

public class AlreadyExistIdException extends RuntimeException{
    public AlreadyExistIdException(String message){
        super(message);
    }
}
