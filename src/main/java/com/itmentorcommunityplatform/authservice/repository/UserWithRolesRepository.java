package com.itmentorcommunityplatform.authservice.repository;

import com.itmentorcommunityplatform.authservice.entity.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserWithRolesRepository extends CrudRepository<UserRole, Integer> {

    List<UserRole> findByUserIdIn(List<Integer> userId);

}
