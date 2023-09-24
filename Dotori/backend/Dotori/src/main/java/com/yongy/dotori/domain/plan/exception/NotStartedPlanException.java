package com.yongy.dotori.domain.plan.exception;

public class NotStartedPlanException extends RuntimeException{
    public NotStartedPlanException(String message){
        super(message);
    }
}
