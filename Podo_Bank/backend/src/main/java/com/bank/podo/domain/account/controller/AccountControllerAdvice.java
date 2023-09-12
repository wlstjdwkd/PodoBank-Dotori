package com.bank.podo.domain.account.controller;

import com.bank.podo.domain.account.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountControllerAdvice {

    @ExceptionHandler(PasswordRetryCountExceededException.class)
    public ResponseEntity<String> handlePasswordRetryCountExceededException(PasswordRetryCountExceededException e) {
        return ResponseEntity.status(429).body(e.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(AccountPasswordFormatException.class)
    public ResponseEntity<String> handleAccountPasswordFormatException(AccountPasswordFormatException e) {
        return ResponseEntity.status(422).body(e.getMessage());
    }

    @ExceptionHandler(AccountUserNotMatchException.class)
    public ResponseEntity<String> handleAccountUserNotMatchException(AccountUserNotMatchException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(InsufficientBalanceException e) {
        return ResponseEntity.status(402).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleAccountLockedException(AccountLockedException e) {
        return ResponseEntity.status(423).body(e.getMessage());
    }
}

