package com.itmentorcommunityplatform.authservice.dummyAuth;

import com.itmentorcommunityplatform.authservice.auth.AuthResponseDto;
import com.itmentorcommunityplatform.authservice.auth.JwtService;
import com.itmentorcommunityplatform.authservice.auth.UserResponseDto;
import com.itmentorcommunityplatform.authservice.entity.User;
import com.itmentorcommunityplatform.authservice.mapper.UserMapper;
import com.itmentorcommunityplatform.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DummyAuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public AuthResponseDto authenticateDummy(DummyRequestDto requestDto) {
        try {
            log.info("|Dummy| Authenticate process started");

            User user = userRepository.findUserById(requestDto.userId())
                    .orElseThrow(() -> new RuntimeException("User with id = {} not found"));

            String token = jwtService.generateToken(user.getTelegramUserId());
            log.info("JWT token generated for userId={}", user.getId());

            UserResponseDto userResponseDto = userMapper.toDto(user);
            log.info("|Dummy| User successfully authenticated ");

            return new AuthResponseDto(token, userResponseDto);
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }
}
