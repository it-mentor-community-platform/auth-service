package com.itmentorcommunityplatform.authservice.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateUserRolesRequest(
        @JsonProperty(value = "roles")
        @NotNull
        List<String> roles
) {
}
