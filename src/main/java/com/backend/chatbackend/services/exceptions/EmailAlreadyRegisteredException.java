package com.backend.chatbackend.services.exceptions;

public class EmailAlreadyRegisteredException extends Exception {
    public EmailAlreadyRegisteredException() {
        super("Email already registered");
    }
}
