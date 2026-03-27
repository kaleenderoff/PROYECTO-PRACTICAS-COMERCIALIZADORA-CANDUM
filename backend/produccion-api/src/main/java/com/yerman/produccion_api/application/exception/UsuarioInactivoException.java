package com.yerman.produccion_api.application.exception;

public class UsuarioInactivoException extends RuntimeException {

    public UsuarioInactivoException(String message) {
        super(message);
    }
}
