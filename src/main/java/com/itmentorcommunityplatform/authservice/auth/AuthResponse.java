package com.itmentorcommunityplatform.authservice.auth;

public record AuthResponse(String accessToken,UserResponseDto userDto) {
}
