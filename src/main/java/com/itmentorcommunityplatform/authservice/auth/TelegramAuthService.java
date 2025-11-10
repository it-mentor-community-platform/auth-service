package com.itmentorcommunityplatform.authservice.auth;

import com.itmentorcommunityplatform.authservice.entity.User;
import com.itmentorcommunityplatform.authservice.mapper.UserMapper;
import com.itmentorcommunityplatform.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramAuthService {

    private final TelegramInitDataValidator validator;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${telegram.init-data-expiration-seconds}")
    private long expirationSeconds;

    public AuthResponseDto authenticateByTelegram(String initData) {
        try {
            log.info("Starting Telegram authentication process...");
            Long telegramUserId = validator.extractUserFromInitData(initData, expirationSeconds);
            log.info("InitData validated successfully for telegramUserId={}", telegramUserId);

            User user = userRepository.findByTelegramUserId(telegramUserId)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setTelegramUserId(telegramUserId);
                        userRepository.save(newUser);
                        log.info("New user created with id={}", newUser.getId());
                        return newUser;
                    });

            String token = jwtService.generateToken(telegramUserId);
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
}
