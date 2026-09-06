package com.itmentorcommunityplatform.authservice.auth;

import com.itmentorcommunityplatform.authservice.docs.auth.AuthControllerDocs;
import jakarta.validation.Valid;
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
    @AuthControllerDocs
    public ResponseEntity<Void> authenticate(@Valid @RequestBody TelegramAuthRequestDto request) {
        AuthResponseDto response = telegramAuthService.authenticateByTelegram(request.initDataRaw());
        return ResponseEntity.ok()
                .header("X-Access-Token", response.accessToken())
                .build();
    }
}
