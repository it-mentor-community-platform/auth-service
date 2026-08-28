package com.itmentorcommunityplatform.authservice.auth;

import jakarta.validation.constraints.NotBlank;

public record TelegramAuthRequestDto(
        @NotBlank(message = "initDataRaw must not be blank")
        String initDataRaw
) {
}
