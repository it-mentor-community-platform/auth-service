package com.itmentorcommunityplatform.authservice.auth;

import com.itmentorcommunityplatform.authservice.entity.User;
import com.itmentorcommunityplatform.authservice.kafka.AuthEventProducer;
import com.itmentorcommunityplatform.authservice.kafka.UserCreatedEvent;
import com.itmentorcommunityplatform.authservice.mapper.UserMapper;
import com.itmentorcommunityplatform.authservice.repository.UserRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramAuthService {

    private final TelegramInitDataValidator validator;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final AuthEventProducer kafkaEventProducer;

    @Value("${telegram.init-data-expiration-seconds}")
    private long expirationSeconds;

    @Transactional
    public AuthResponseDto authenticateByTelegram(String initData) {
        try {
            log.info("Starting Telegram authentication process...");
            TelegramInitData telegramInitData = validator.validateAndParse(initData, expirationSeconds);
            String telegramUsername = telegramInitData.telegramUsername();
            Long telegramUserId = telegramInitData.telegramUserId();
            String firstName = telegramInitData.firstName();
            String lastName = telegramInitData.lastName();

            log.info("InitData validated successfully for telegramUserId={}", telegramUserId);

            User user = userRepository.findByTelegramUserId(telegramUserId)
                    .orElseGet(() -> createNewUser(telegramUserId, telegramUsername, firstName, lastName));

            var userRoles = userRoleRepository.findRolesByUserId(user.getId());
            log.info("User Roles = {}",userRoles);

            String token = jwtService.generateToken(telegramUserId, userRoles, telegramUsername);
            log.info("JWT token generated for userId={}", user.getId());

            UserResponseDto userResponseDto = userMapper.toDto(user);
            log.info("User successfully authenticated via Telegram");

            return new AuthResponseDto(token, userResponseDto);
        } catch (InvalidInitDataException e) {
            log.error("Telegram authentication failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

    private User createNewUser(Long telegramUserId, String telegramUsername, String firstName, String lastName) {
        User newUser = new User();
        newUser.setTelegramUserId(telegramUserId);
        User saved = userRepository.save(newUser);
        log.info("New user created with id={}", saved.getId());
        kafkaEventProducer.sendUserCreated(new UserCreatedEvent(telegramUserId, telegramUsername, firstName, lastName));
        log.info("A message about creating the new user was sent to kafka.");
        return saved;
    }
}
