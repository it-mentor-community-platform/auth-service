package com.itmentorcommunityplatform.authservice.internalUser;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
