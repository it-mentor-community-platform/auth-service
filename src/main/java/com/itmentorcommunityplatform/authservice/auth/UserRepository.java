package com.itmentorcommunityplatform.authservice.auth;

import com.itmentorcommunityplatform.authservice.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends CrudRepository<User,Long> {

    User findByTelegramUserId(Long telegramUserId);

}
