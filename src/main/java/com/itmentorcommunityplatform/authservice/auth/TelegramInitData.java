package com.itmentorcommunityplatform.authservice.auth;

public record TelegramInitData(
        Long telegramUserId,
        String telegramUsername
) {
}
