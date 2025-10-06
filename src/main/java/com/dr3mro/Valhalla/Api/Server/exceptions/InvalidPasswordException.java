package com.dr3mro.Valhalla.Api.Server.exceptions;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
