package com.itmentorcommunityplatform.authservice.internalUser;

import java.util.List;

public record UserWithRolesDto(Long telegramUserId, List<String> roleName) {}
