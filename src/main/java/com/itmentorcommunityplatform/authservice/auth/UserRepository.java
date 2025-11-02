package com.itmentorcommunityplatform.authservice.auth;

import com.itmentorcommunityplatform.authservice.configuration.JdbcConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public User findByTelegramUserId(Long telegramUserId) {

        String sql = "SELECT id, telegram_user_id FROM users WHERE telegram_user_id = ?";

        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setTelegramUserId(rs.getLong("telegram_user_id"));
            return user;
        }, telegramUserId);

        return users.isEmpty() ? null : users.get(0);
    }


    public User save(User user) {
        String sql = "INSERT INTO users (telegram_user_id) VALUES (?) RETURNING id";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, user.getTelegramUserId());
        user.setId(id);
        return user;
    }
}
