package com.itmentorcommunityplatform.authservice.dummyAuth;

import jakarta.validation.constraints.NotNull;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(@NotNull Long telegramUserId) {
        super("User with telegram id = " + telegramUserId + " not found");
    }

    public UserNotFoundException() {
        super("User not found");
    }
}
