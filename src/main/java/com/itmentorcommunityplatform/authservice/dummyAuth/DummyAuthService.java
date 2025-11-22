package com.itmentorcommunityplatform.authservice.dummyAuth;

import com.itmentorcommunityplatform.authservice.auth.AuthResponseDto;
import com.itmentorcommunityplatform.authservice.auth.JwtService;
import com.itmentorcommunityplatform.authservice.auth.UserResponseDto;
import com.itmentorcommunityplatform.authservice.entity.User;
import com.itmentorcommunityplatform.authservice.mapper.UserMapper;
import com.itmentorcommunityplatform.authservice.repository.UserRepository;
import com.itmentorcommunityplatform.authservice.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DummyAuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;


    @Transactional(readOnly = true)
    public AuthResponseDto authenticateDummy(DummyRequestDto requestDto) {
            log.debug("|Dummy| Authenticate process started");

            User user = userRepository.findById(requestDto.userId())
                    .orElseThrow(() -> new UserNotFoundException(requestDto.userId()));

            var userRoles = userRoleRepository.findRolesByUserId(user.getId());
            log.info("User Roles = {}",userRoles);
            String token = jwtService.generateToken(user.getTelegramUserId(),userRoles);
            log.info("JWT token generated for userId={}", user.getId());

            UserResponseDto userResponseDto = userMapper.toDto(user);
            log.info("|Dummy| User successfully authenticated ");

            return new AuthResponseDto(token, userResponseDto);
    }
}
