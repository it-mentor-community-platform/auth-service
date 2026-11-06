package com.itmentorcommunityplatform.authservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "profile-service")
public record ProfileServiceProperties(
        String url,
        Duration connectTimeout,
        Duration readTimeout
) {
}