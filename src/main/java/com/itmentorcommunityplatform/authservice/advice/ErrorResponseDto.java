package com.itmentorcommunityplatform.authservice.advice;

public record ErrorResponseDto(String timestamp,
                               int status,
                               String error,
                               String message,
                               String path) {
}
