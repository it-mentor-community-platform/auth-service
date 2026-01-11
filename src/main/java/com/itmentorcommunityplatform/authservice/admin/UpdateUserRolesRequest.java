package com.itmentorcommunityplatform.authservice.admin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UpdateUserRolesRequest(
        @JsonProperty(value = "roles")
        List<String> roles
) {
}
