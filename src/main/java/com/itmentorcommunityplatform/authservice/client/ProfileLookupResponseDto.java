package com.itmentorcommunityplatform.authservice.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProfileLookupResponseDto(
        @JsonProperty("telegram_user_id") Long telegramUserId,
        Details details
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Details(
            @JsonProperty("telegram_url") String telegramUrl
    ) {
    }
}