package com.itmentorcommunityplatform.authservice.repository;

import com.itmentorcommunityplatform.authservice.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role,Integer> {

    List<Role> findByNameIn(Collection<String> names);

}
