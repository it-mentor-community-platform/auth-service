package com.itmentorcommunityplatform.authservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN(1),
    MENTOR(3),
    STUDENT(2);

    private final int roleId;
}
