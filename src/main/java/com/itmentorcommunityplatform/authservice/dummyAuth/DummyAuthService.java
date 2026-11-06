package com.itmentorcommunityplatform.authservice.dummyAuth;

import com.itmentorcommunityplatform.authservice.auth.AuthResponseDto;
import com.itmentorcommunityplatform.authservice.auth.JwtService;
import com.itmentorcommunityplatform.authservice.auth.UserResponseDto;
import com.itmentorcommunityplatform.authservice.client.ProfileLookupResponseDto;
import com.itmentorcommunityplatform.authservice.client.ProfileNotFoundException;
import com.itmentorcommunityplatform.authservice.client.ProfileServiceClient;
import com.itmentorcommunityplatform.authservice.entity.Role;
import com.itmentorcommunityplatform.authservice.entity.User;
import com.itmentorcommunityplatform.authservice.internalUser.InvalidRoleException;
import com.itmentorcommunityplatform.authservice.mapper.UserMapper;
import com.itmentorcommunityplatform.authservice.repository.RoleRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DummyAuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final ProfileServiceClient profileClient;


    @Transactional(readOnly = true)
    public AuthResponseDto authenticateDummy(DummyRequestDto requestDto) {
        log.debug("|Dummy| Authenticate process started");

        var rolesNames = requestDto.rolesNames();
        ResolvedIdentity identity = resolveIdentity(requestDto);
        var telegramUserId = identity.telegramUserId();


        User user = userRepository.findByTelegramUserId(telegramUserId)
                .orElseThrow(() -> new UserNotFoundException(telegramUserId));

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
        log.info("User Roles = {}", rolesNames);
        String token = jwtService.generateToken(user.getTelegramUserId(), rolesNames, identity.telegramUsername());
        log.info("JWT token generated for userId={}", user.getId());

        UserResponseDto userResponseDto = userMapper.toDto(user);
        log.info("|Dummy| User successfully authenticated ");

        return new AuthResponseDto(token, userResponseDto);
    }

    private ResolvedIdentity resolveIdentity(DummyRequestDto dto) {
        if (dto.telegramUserId() != null) {
            return new ResolvedIdentity(dto.telegramUserId(), findUsername(dto.telegramUserId()));
        }

        ProfileLookupResponseDto profile = dto.telegramUrl() != null ?
                profileClient.findByTelegramUrl(dto.telegramUrl()) :
                profileClient.findByGithubUrl(dto.githubProfileUrl());


        return new ResolvedIdentity(
                profile.telegramUserId(),
                extractUsername(profile.details().telegramUrl())
        );
    }

    private String findUsername(Long telegramUserId) {
        try {
            ProfileLookupResponseDto dto = profileClient.findByTelegramUserId(telegramUserId);
            return extractUsername(dto.details().telegramUrl());
        } catch (ProfileNotFoundException ex) {
            return null;
        }
    }

    private String extractUsername(String telegramUrl) {
        if (telegramUrl == null) {
            return null;
        }
        int beginIndex = telegramUrl.lastIndexOf('/') + 1;

        return telegramUrl.substring(beginIndex);
    }

    private record ResolvedIdentity(Long telegramUserId, String telegramUsername) {
    }
}
