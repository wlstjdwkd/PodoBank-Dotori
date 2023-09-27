package com.yongy.dotoriuserservice.domain.account.exception;

public class ExistAccountNumberException extends RuntimeException{
    public ExistAccountNumberException(String message){
        super(message);
    }
}
