package com.itmentorcommunityplatform.authservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    private Integer id;

    @Column("telegram_user_id")
    private Long telegramUserId;

    public User(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

}
