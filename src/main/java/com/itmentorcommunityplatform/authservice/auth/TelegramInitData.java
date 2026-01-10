package com.itmentorcommunityplatform.authservice.auth;

public record TelegramInitData(
        Long telegramUserId,
        String telegramUsername,
        String firstName,
        String lastName
) {
}
