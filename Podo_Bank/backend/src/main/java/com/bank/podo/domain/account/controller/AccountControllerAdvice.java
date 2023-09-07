package com.bank.podo.domain.account.controller;

import com.bank.podo.domain.account.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountControllerAdvice {

    @ExceptionHandler(PasswordRetryCountExceededException.class)
    public ResponseEntity<String> handlePasswordRetryCountExceededException(PasswordRetryCountExceededException e) {
        return ResponseEntity.badRequest().body("Password retry count exceeded.");
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
        return ResponseEntity.status(404).body("Account not found.");
    }

    @ExceptionHandler(AccountPasswordFormatException.class)
    public ResponseEntity<String> handleAccountPasswordFormatException(AccountPasswordFormatException e) {
        return ResponseEntity.badRequest().body("Invalid account password format.");
    }

    @ExceptionHandler(AccountUserNotMatchException.class)
    public ResponseEntity<String> handleAccountUserNotMatchException(AccountUserNotMatchException e) {
        return ResponseEntity.badRequest().body("Not the account owner.");
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(InsufficientBalanceException e) {
        return ResponseEntity.badRequest().body("Insufficient balance.");
    }
}

