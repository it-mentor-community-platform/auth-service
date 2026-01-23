package com.itmentorcommunityplatform.authservice.internalUser;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserWithRolesDto(@JsonProperty("telegram_user_id")
                               Long telegramUserId,
                               @JsonProperty("roles")
                               List<String> roleName) {
}
