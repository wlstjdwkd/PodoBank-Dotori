package com.yongy.dotoripurposeservice.domain.user.exception;

public class InvalidIdException extends RuntimeException{
    public InvalidIdException(String message){
        super(message);
    }
}
