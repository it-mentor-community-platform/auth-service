package com.itmentorcommunityplatform.authservice.repository;

import com.itmentorcommunityplatform.authservice.entity.UserRole;
import com.itmentorcommunityplatform.authservice.internalUser.UserWithRolesDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserWithRolesRepository extends CrudRepository<UserRole, Integer> {
    
    @Query("""
                select us.telegram_user_id as telegram_user_id,
                       array_agg(r.name)    as role_name
                from users_roles usro
                join users us on usro.user_id = us.id
                join roles r on usro.role_id = r.id
                where us.telegram_user_id in (:telegramUserIds)
                group by us.telegram_user_id
            """)
    List<UserWithRolesDto> findUserWithRolesByTelegramUserIds(List<Long> telegramUserIds);

}
