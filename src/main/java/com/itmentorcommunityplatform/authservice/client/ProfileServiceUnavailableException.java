package com.itmentorcommunityplatform.authservice.client;

public class ProfileServiceUnavailableException extends RuntimeException {

    public ProfileServiceUnavailableException(String message) {
        super(message);
    }

    public ProfileServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}