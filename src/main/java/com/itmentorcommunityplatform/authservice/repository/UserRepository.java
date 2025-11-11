package com.itmentorcommunityplatform.authservice.repository;

import com.itmentorcommunityplatform.authservice.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  UserRepository extends CrudRepository<User,Integer> {

    Optional<User> findByTelegramUserId(Long telegramUserId);

}
