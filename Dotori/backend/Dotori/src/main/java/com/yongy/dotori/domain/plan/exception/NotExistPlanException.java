package com.yongy.dotori.domain.plan.exception;

public class NotExistPlanException extends RuntimeException{
    public NotExistPlanException(String message){
        super(message);
    }
}
