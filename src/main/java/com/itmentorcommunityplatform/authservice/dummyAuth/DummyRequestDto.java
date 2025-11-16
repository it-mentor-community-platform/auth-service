package com.itmentorcommunityplatform.authservice.dummyAuth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Request DTO for dummy authentication")
public record DummyRequestDto(
        @NotNull
        @JsonProperty("user_id")
        @Min(1)
        Integer userId,
        @NotEmpty
        @JsonProperty("roles")
        List<String> rolesNames) {
}
