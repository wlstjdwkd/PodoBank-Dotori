package com.bank.podo.domain.openbank.controller;

import com.bank.podo.domain.openbank.exception.FintechServiceNotFoundException;
import com.bank.podo.domain.openbank.exception.VerificationCodeNotMathchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OpenBankingControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<Void> handleFintechServiceNotFoundException(FintechServiceNotFoundException e) {
        return ResponseEntity.status(404).build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleVerificationCodeNotMathchException(VerificationCodeNotMathchException e) {
        return ResponseEntity.status(400).build();
    }

}
