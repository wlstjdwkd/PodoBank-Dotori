package com.yongy.dotorimainservice.domain.user.exception;

public class InvalidIdException extends RuntimeException{
    public InvalidIdException(String message){
        super(message);
    }
}
