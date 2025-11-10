package com.itmentorcommunityplatform.authservice.dummyAuth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DummyRequestDto(
        @NotNull
        @JsonProperty("user_id")
        Integer userId,
        @NotEmpty
        @JsonProperty("roles")
        List<String> rolesNames) {
}
