package com.bank.podo.domain.user.exception;

public class PasswordFromatException extends RuntimeException {
    public PasswordFromatException(String message) {
        super(message);
    }
}
