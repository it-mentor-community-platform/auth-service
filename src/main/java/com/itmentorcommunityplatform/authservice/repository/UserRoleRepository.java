package com.itmentorcommunityplatform.authservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRoleRepository {
    private final JdbcTemplate template;

    public void insertUserRole(Integer userId, List<Integer> roleIds){
       String sql =  "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)";
       template.batchUpdate(sql,roleIds,roleIds.size(),
               (ps,roleId) -> {
           ps.setInt(1,userId);
           ps.setInt(2,roleId);
       } );
    }

    public void deleteRolesByUserId(Integer userId) {
        template.update("DELETE FROM users_roles WHERE user_id = ?", userId);
    }
}
