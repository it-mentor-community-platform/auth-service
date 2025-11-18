package com.itmentorcommunityplatform.authservice.advice;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard error response")
public record ErrorResponseDto(String timestamp,
                               int status,
                               String error,
                               String message,
                               String path) {
}
