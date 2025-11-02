package com.itmentorcommunityplatform.authservice.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TelegramAuthService telegramAuthService;

    @PostMapping("/by-telegram")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody String initData) {
        AuthResponse response = telegramAuthService.authenticateByTelegram(initData);
        return ResponseEntity.ok(response);
    }
}
