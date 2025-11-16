package com.itmentorcommunityplatform.authservice.internalUser;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


@Schema(description = "Request DTO for importing internal user")
public record UserUpsertRequestDto(
        @NotNull
        @JsonProperty("telegram_user_id")
        Long telegramUserId,
        @NotEmpty
        List<String>roles
) {
}
