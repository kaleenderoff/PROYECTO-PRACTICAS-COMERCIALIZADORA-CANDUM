package com.yerman.produccion_api.application.exception;

public class PasswordIncorrectaException extends RuntimeException {

    public PasswordIncorrectaException(String message) {
        super(message);
    }
}
