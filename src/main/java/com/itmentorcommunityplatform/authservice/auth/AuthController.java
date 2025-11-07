package com.itmentorcommunityplatform.authservice.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<Void> authenticate(@RequestBody String initData) {
        AuthResponseDto response = telegramAuthService.authenticateByTelegram(initData);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + response.accessToken())
                .build();
    }
}
