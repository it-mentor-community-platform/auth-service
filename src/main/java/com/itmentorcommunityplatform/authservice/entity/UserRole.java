package com.itmentorcommunityplatform.authservice.entity;


import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "users_roles")
public class UserRole {

    @Column("user_id")
    private Integer userId;

    @Column("role_id")
    private Integer roleId;

}
