package com.itmentorcommunityplatform.authservice.auth;

public record AuthResponseDto(String accessToken, UserResponseDto userDto) {
}
