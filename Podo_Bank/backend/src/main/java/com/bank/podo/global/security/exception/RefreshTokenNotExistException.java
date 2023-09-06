package com.bank.podo.global.security.exception;

public class RefreshTokenNotExistException extends RuntimeException {
    public RefreshTokenNotExistException(String message) {
        super(message);
    }
}
