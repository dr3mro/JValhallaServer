package com.dr3mro.Valhalla.Api.Server.exceptions;

public class UserIdNotProvidedException extends RuntimeException {

    public UserIdNotProvidedException() {
        super("User ID must be provided");
    }
}
