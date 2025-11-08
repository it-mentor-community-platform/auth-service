package com.itmentorcommunityplatform.authservice.internalUser;

import com.itmentorcommunityplatform.authservice.entity.User;
import com.itmentorcommunityplatform.authservice.repository.RoleRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


        var user = userRepository.findByTelegramUserId(telegramId);
        if (user != null) {
            throw new UserAlreadyExistsException("User with telegramUserId " + telegramId + " already exists");
        }
        user = new User();
        user.setTelegramUserId(telegramId);
        userRepository.save(user);
        log.info("New user created with id={}", user.getId());


        var roles = roleRepository.findByNameIn(rolesNames);
        log.info("Roles that was found: {}", roles.stream().toList());
        if (roles.size() != rolesNames.size()) {
            throw new InvalidRoleException("Unknown roles: " +
                                           rolesNames.stream()
                                                   .filter(
                                                           r -> roles
                                                                   .stream()
                                                                   .noneMatch(
                                                                           role -> role.getName().equals(r)
                                                                   )
                                                   )
                                                   .toList());
        }
        var rolesId = roles.stream().map(r -> r.getId()).toList();
        userRoleRepository.insertUserRole(user.getId(), rolesId);
        log.info("User_id and Roles_id were insert to Users_Roles table successfully.");
    }
}
