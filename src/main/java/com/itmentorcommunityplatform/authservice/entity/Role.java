package com.itmentorcommunityplatform.authservice.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "roles")
public class Role {
    @Id
    private Integer id;

    private String name;
}
