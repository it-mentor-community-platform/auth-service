package com.itmentorcommunityplatform.authservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRoleRepository {
    private final JdbcTemplate template;

    public void insertUserRole(Integer userId, List<Integer> roleIds) {
        String sql = """
                INSERT INTO users_roles (user_id, role_id)
                VALUES (?, ?)
                ON CONFLICT (user_id, role_id) DO NOTHING 
                """;
        template.batchUpdate(sql, roleIds, roleIds.size(),
                (ps, roleId) -> {
                    ps.setInt(1, userId);
                    ps.setInt(2, roleId);
                });
    }

    public List<String> findRolesByUserId(Integer userId) {
        String sql = """
        SELECT r.name
        FROM users_roles ur
        JOIN roles r ON r.id = ur.role_id
        WHERE ur.user_id = ?
        """;

        return template.query(
                sql,
                (rs, rowNum) -> rs.getString("name"),
                userId
        );
    }

    public void dropAllRolesExceptAdmin(int userId) {
        String sql = """
        DELETE FROM users_roles
        WHERE user_id = ? AND role_id != (SELECT id FROM roles WHERE name = 'ADMIN')
        """;
        template.update(sql, userId);
    }
}
