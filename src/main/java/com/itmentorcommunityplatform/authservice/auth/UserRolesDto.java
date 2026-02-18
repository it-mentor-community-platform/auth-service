package com.itmentorcommunityplatform.authservice.auth;

import java.util.List;

public record UserRolesDto(
        Integer id,
        Long telegramUserId,
        List<String> roles
) {
}
