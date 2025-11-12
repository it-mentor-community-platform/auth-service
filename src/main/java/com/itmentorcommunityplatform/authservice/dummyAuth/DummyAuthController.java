package com.itmentorcommunityplatform.authservice.dummyAuth;


import com.itmentorcommunityplatform.authservice.auth.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Profile("!prod")
public class DummyAuthController {
    private final   DummyAuthService dummyAuthService;

    @PostMapping("/by-dummy")
    public ResponseEntity<Void> dummyAuth(@RequestBody @Validated DummyRequestDto requestDto){
        AuthResponseDto response = dummyAuthService.authenticateDummy(requestDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.accessToken())
                .build();
    }
}
