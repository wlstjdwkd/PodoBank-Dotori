package com.yongy.dotori.domain.plan.exception;

public class PaymentUpdateBeforeException extends RuntimeException{
    public PaymentUpdateBeforeException(String message){
        super(message);
    }
}
