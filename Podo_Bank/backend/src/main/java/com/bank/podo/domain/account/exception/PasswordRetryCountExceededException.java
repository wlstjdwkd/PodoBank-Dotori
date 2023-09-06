package com.bank.podo.domain.account.exception;

public class PasswordRetryCountExceededException extends RuntimeException {
    public PasswordRetryCountExceededException(String message) {
        super(message);
    }
}
