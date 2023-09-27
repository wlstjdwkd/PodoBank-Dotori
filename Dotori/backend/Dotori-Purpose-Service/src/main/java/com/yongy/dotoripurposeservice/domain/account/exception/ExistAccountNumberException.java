package com.yongy.dotoripurposeservice.domain.account.exception;

public class ExistAccountNumberException extends RuntimeException{
    public ExistAccountNumberException(String message){
        super(message);
    }
}
