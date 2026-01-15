package com.itmentorcommunityplatform.authservice.internalUser;

import com.itmentorcommunityplatform.authservice.entity.Role;
import com.itmentorcommunityplatform.authservice.entity.User;
import com.itmentorcommunityplatform.authservice.entity.UserRole;
import com.itmentorcommunityplatform.authservice.kafka.AuthEventProducer;
import com.itmentorcommunityplatform.authservice.kafka.UserCreatedEvent;
import com.itmentorcommunityplatform.authservice.repository.RoleRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRoleRepository;
import com.itmentorcommunityplatform.authservice.repository.UserWithRolesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthEventProducer kafkaEventProducer;
    private final UserWithRolesRepository userWithRolesRepository;

    @Transactional
    public InternalUserResultUpsertDto upsertUser(UserUpsertRequestDto requestDto) {
        var telegramId = requestDto.telegramUserId();
        var rolesNames = requestDto.roles();


        var userOptional = userRepository.findByTelegramUserId(telegramId);
        User user;
        boolean created;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            created = false;
        } else {
            user = new User();
            user.setTelegramUserId(telegramId);
            userRepository.save(user);
            created = true;
            log.info("New user created with id={}", user.getId());
        }


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

        if (created) {
            kafkaEventProducer.sendUserCreated(new UserCreatedEvent(telegramId, null, null, null));
            log.info("A message about creating the new user was sent to kafka.");
        }

        return new InternalUserResultUpsertDto(user.getTelegramUserId(), created);
    }

    public List<UserWithRolesDto> getListUsers(List<Long> telegramUserIds) {

        validation(telegramUserIds);

        List<UserWithRolesDto> userWithRolesDto = new ArrayList<>();

        List<User> listUsers = userRepository.findAllByTelegramUserIdIn(telegramUserIds);

        List<Integer> listUserIds = listUsers.stream().map(User::getId).toList();

        List<UserRole> listUserRoles = userWithRolesRepository.findByUserIdIn(listUserIds);

        Map<Integer, List<Integer>> mapUserIdToRoleIds = listUserRoles.stream()
                .collect(Collectors.groupingBy(
                        UserRole::getUserId,
                        Collectors.mapping(UserRole::getRoleId, Collectors.toList())
                ));

        Set<Integer> allRoleIds = mapUserIdToRoleIds.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        List<Role> listRoles = roleRepository.findAllByIdIn(allRoleIds);

        Map<Integer, String> mapRoles = listRoles.stream()
                .collect(Collectors.toMap(Role::getId, Role::getName));


        for (User user : listUsers) {
            List<String> listUserRoleNames = mapUserIdToRoleIds
                    .getOrDefault(user.getId(), Collections.emptyList())
                    .stream()
                    .map(mapRoles::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            userWithRolesDto.add(new UserWithRolesDto(user.getTelegramUserId(), listUserRoleNames));
        }

        return userWithRolesDto;
    }

    private  void validation(List<Long> telegramUserIds){

        if (telegramUserIds.isEmpty() ) {
            throw new IllegalArgumentException("telegram_user_ids must not be empty");
        }

        for (Long id : telegramUserIds) {
            if (id == null ) {
                throw new IllegalArgumentException("telegram_user_ids must not contain nulls");
            }
            if (id<=0){
                throw new IllegalArgumentException("telegram_user_ids must be positive");
            }
        }
    }
}
