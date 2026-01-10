package com.itmentorcommunityplatform.authservice.admin;

import com.itmentorcommunityplatform.authservice.constant.Role;
import com.itmentorcommunityplatform.authservice.dummyAuth.UserNotFoundException;
import com.itmentorcommunityplatform.authservice.entity.User;
import com.itmentorcommunityplatform.authservice.internalUser.InvalidRoleException;
import com.itmentorcommunityplatform.authservice.repository.UserRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public void changeUserRoles(Long telegramUserId,
                                UpdateUserRolesRequest dto,
                                HttpServletRequest httpRequest) {

        checkUserRole(httpRequest, Role.ADMIN);

        userRoleRepository.lockUserByTelegramId(telegramUserId);

        List<Role> targetRoles = dto.roles().stream()
                .map(this::parseRole)
                .toList();

        User user = userRepository.findByTelegramUserId(telegramUserId)
                .orElseThrow(() -> {
                    log.warn("Role update failed: User with telegramUserId {} not found", telegramUserId);
                    return new UserNotFoundException();
                });

        Integer userId = user.getId();

        List<String> userRoles = userRoleRepository.findRolesByUserId(userId);
        boolean isCurrentlyAdmin = userRoles.contains(Role.ADMIN.name());
        boolean wantsToBeAdmin = targetRoles.contains(Role.ADMIN);

        if (isCurrentlyAdmin != wantsToBeAdmin) {
            log.warn("Forbidden: Attempt to modify ADMIN role for userId: {}. Operation rejected.", userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Modifying ADMIN role is not allowed");
        }

        log.info("Dropping old roles for userId: {}", userId);
        userRoleRepository.dropAllRolesExceptAdmin(userId);

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

    private void checkUserRole(HttpServletRequest request, Role role) {
        String rolesHeader = request.getHeader("X-User-Roles");

        if (rolesHeader == null) {
            log.warn("Access denied: No roles provided in request headers");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No roles provided");
        }

        boolean hasRole = Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .anyMatch(r -> r.equalsIgnoreCase(role.name()));

        if (!hasRole) {
            log.warn("Access denied: Requester does not have required role: {}", role);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Missing role: " + role);
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