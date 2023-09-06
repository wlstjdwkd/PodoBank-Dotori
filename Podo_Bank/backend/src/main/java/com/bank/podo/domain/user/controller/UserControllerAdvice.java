package com.bank.podo.domain.user.controller;

import com.bank.podo.domain.user.exception.AlreadyUsedUsernameException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler
    public String handleAlreadyUsedUsernameException(AlreadyUsedUsernameException e) {
        return "error";
    }
}
