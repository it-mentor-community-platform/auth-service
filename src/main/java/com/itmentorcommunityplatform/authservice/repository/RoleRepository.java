package com.itmentorcommunityplatform.authservice.repository;

import com.itmentorcommunityplatform.authservice.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role,Integer> {

    default List<Role> findByNames(List<String> names){
        List<Role> allRoles = (List<Role>) findAll();
        return allRoles.stream()
                .filter(r -> names.contains(r.getName()))
                .toList();
    }
}
