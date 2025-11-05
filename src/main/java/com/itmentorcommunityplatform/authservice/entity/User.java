package com.itmentorcommunityplatform.authservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class User {
    @Id
    private Integer id;

    private Long telegramUserId;
}
