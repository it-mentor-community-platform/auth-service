package com.itmentorcommunityplatform.authservice.admin;

import com.itmentorcommunityplatform.authservice.constant.Role;
import com.itmentorcommunityplatform.authservice.dummyAuth.UserNotFoundException;
import com.itmentorcommunityplatform.authservice.entity.User;
import com.itmentorcommunityplatform.authservice.internalUser.InvalidRoleException;
import com.itmentorcommunityplatform.authservice.kafka.AuthEventProducer;
import com.itmentorcommunityplatform.authservice.kafka.UserAuthenticatedEvent;
import com.itmentorcommunityplatform.authservice.repository.UserRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthEventProducer kafkaEventProducer;

    @Transactional
    public void changeUserRoles(Long telegramUserId,
                                List<String> headerRoles,
                                UpdateUserRolesRequest request) {

        verifyIsAdmin(headerRoles);

        List<Role> targetRoles = request.roles().stream()
                .map(this::parseRole)
                .toList();

        User user = userRepository.findByTelegramUserId(telegramUserId)
                .orElseThrow(() -> {
                    log.warn("Role update failed: User with telegramUserId {} not found", telegramUserId);
                    return new UserNotFoundException();
                });

        userRoleRepository.lockUserByTelegramId(telegramUserId);

        Integer userId = user.getId();

        List<String> userRoles = userRoleRepository.findRolesByUserId(userId);

        preventAdminRoleChange(userRoles, targetRoles);

        refreshUserRoles(userId, targetRoles);

        publishUserRolesChangedEvent(telegramUserId, targetRoles);
    }

    private void publishUserRolesChangedEvent(Long telegramUserId, List<Role> targetRoles) {
        List<String> rolesToPublish = targetRoles.stream().map(Role::name).distinct().toList();
        kafkaEventProducer.sendUserAuthenticated(
                new UserAuthenticatedEvent(telegramUserId, null, null, null, rolesToPublish));
        log.info("Successfully sent change roles message to kafka, for telegramUserId: {}, roles: {}", telegramUserId, targetRoles);
    }

    private void refreshUserRoles(Integer userId, List<Role> targetRoles) {
        userRoleRepository.dropAllRolesExceptAdmin(userId);
        log.info("Dropping old roles for userId: {}", userId);

        List<Integer> roleId = targetRoles.stream()
                .filter(role -> role != Role.ADMIN)
                .map(Role::getRoleId)
                .toList();

        if (!roleId.isEmpty()) {
            log.info("Inserting new roles for userId: {}: {}", userId, roleId);
            userRoleRepository.insertUserRole(userId, roleId);
        } else {
            log.debug("No additional roles to insert for userId: {}", userId);
        }
    }

    private void preventAdminRoleChange(List<String> userRoles, List<Role> targetRoles) {
        boolean isCurrentlyAdmin = userRoles.contains(Role.ADMIN.name());
        boolean wantsToBeAdmin = targetRoles.contains(Role.ADMIN);

        if (isCurrentlyAdmin != wantsToBeAdmin) {
            log.warn("Forbidden: Attempt to modify ADMIN role. Operation rejected.");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Modifying ADMIN role is not allowed");
        }
    }

    private void verifyIsAdmin(List<String> roles) {
        boolean isAdmin = roles.stream()
                .map(this::parseRole)
                .anyMatch(Role.ADMIN::equals);

        if (!isAdmin) {
            log.warn("Access denied: Requester does not have required role: {}", Role.ADMIN.name());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Missing role: " + Role.ADMIN.name());
        }
    }

    public Role parseRole(String roleName) {
        try {
            String cleanedName = roleName.replace("ROLE_", "").toUpperCase().trim();
            return Role.valueOf(cleanedName);
        } catch (IllegalArgumentException e) {
            log.error("Failed to parse role: {}. Invalid role name.", roleName);
            throw new InvalidRoleException("Invalid role: %s".formatted(roleName));
        }
    }
}