package com.itmentorcommunityplatform.authservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "users")
public class User {
    @Id
    private Integer id;

    private Long telegramUserId;
}
