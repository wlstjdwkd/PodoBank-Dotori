package com.bank.podo.domain.openbank.controller;

import com.bank.podo.domain.openbank.exception.AlreadyExistUserException;
import com.bank.podo.domain.openbank.exception.FintechServiceNotFoundException;
import com.bank.podo.domain.openbank.exception.VerificationCodeNotMathchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class OpenBankingControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<Void> handleFintechServiceNotFoundException(FintechServiceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleVerificationCodeNotMathchException(VerificationCodeNotMathchException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleAlreadyExistUserException(AlreadyExistUserException e) {
        return ResponseEntity.badRequest().build();
    }

}
