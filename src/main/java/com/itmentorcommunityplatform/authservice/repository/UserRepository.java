package com.itmentorcommunityplatform.authservice.repository;

import com.itmentorcommunityplatform.authservice.auth.UserRolesDto;
import com.itmentorcommunityplatform.authservice.entity.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  UserRepository extends CrudRepository<User,Integer> {

    Optional<User> findByTelegramUserId(Long telegramUserId);

    @Query("""
                SELECT u.id AS id,
                    u.telegram_user_id AS telegram_user_id,
                    COALESCE(ARRAY_AGG(r.name) FILTER (WHERE r.name IS NOT NULL), '{}') AS roles
                FROM users u
                LEFT JOIN users_roles ur ON ur.user_id = u.id
                LEFT JOIN roles r ON r.id = ur.role_id
                WHERE u.telegram_user_id = :telegramUserId
                GROUP BY u.id
            
            """)
    Optional<UserRolesDto> findUserRolesByTelegramUserId(Long telegramUserId);
}
