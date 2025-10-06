package com.dr3mro.Valhalla.Api.Server.exceptions;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("User with email already exists: " + email);
    }
}
