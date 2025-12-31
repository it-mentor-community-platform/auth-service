package com.itmentorcommunityplatform.authservice.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserCreatedEvent (

        @JsonProperty("telegram_user_id")
        long telegramUserId,
        @JsonProperty("telegram_username")
        String telegramUsername,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName
) {}
