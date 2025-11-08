package com.itmentorcommunityplatform.authservice.internalUser;

import com.itmentorcommunityplatform.authservice.entity.Role;
import com.itmentorcommunityplatform.authservice.entity.User;
import com.itmentorcommunityplatform.authservice.repository.RoleRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public void upsertUser(UserUpsertRequestDto requestDto) {
        var telegramId = requestDto.telegramUserId();
        var rolesNames = requestDto.roles();


        var user = userRepository.findByTelegramUserId(telegramId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setTelegramUserId(telegramId);
                    userRepository.save(newUser);
                    log.info("New user created with id={}", newUser.getId());
                    return newUser;
                });



        var roles = roleRepository.findByNameIn(rolesNames);
        log.info("Roles that was found: {}", roles.stream().toList());
        if (roles.size() != rolesNames.size()) {
            String unknownRoles = rolesNames.stream()
                    .filter(role -> roles.stream()
                            .map(Role::getName)
                            .noneMatch(role::equals)
                    )
                    .collect(Collectors.joining(", "));
            throw new InvalidRoleException("Unknown roles: " + unknownRoles);
        }
        var rolesId = roles.stream().map(r -> r.getId()).toList();
        userRoleRepository.insertUserRole(user.getId(), rolesId);
        log.info("User_id and Roles_id were insert to Users_Roles table successfully.");
    }
}
