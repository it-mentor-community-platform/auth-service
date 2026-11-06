package com.itmentorcommunityplatform.authservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProfileServiceClient {
    private static final String BY_TELEGRAM_URL = "/api/profile/internal/profile/by-telegram-url";
    private static final String BY_GITHUB_URL = "/api/profile/internal/profile/by-github-profile-url";
    private static final String BY_PROFILE = "/api/profile/profile";
    private static final String TELEGRAM_ID_HEADER = "X-Telegram-User-Id";

    private final WebClient webClient;

    public ProfileLookupResponseDto findByTelegramUrl(String telegramUrl) {
        return findByUrl(BY_TELEGRAM_URL, telegramUrl, "telegram profile url");
    }

    public ProfileLookupResponseDto findByGithubUrl(String githubUrl) {
        return findByUrl(BY_GITHUB_URL, githubUrl, "github profile url");
    }

    private ProfileLookupResponseDto findByUrl(String path, String url, String urlType) {
        try {
            ProfileLookupResponseDto result = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam("url", "{url}")
                            .build(url))
                    .retrieve()
                    .onStatus(status -> status.isSameCodeAs(HttpStatus.BAD_REQUEST),
                            response -> Mono.error(new ProfileServiceBadRequestException("Invalid %s: %s".formatted(urlType, url))))
                    .onStatus(status -> status.isSameCodeAs(HttpStatus.NOT_FOUND),
                            response -> Mono.error(new ProfileNotFoundException("Profile not found for %s: %s".formatted(urlType, url))))
                    .bodyToMono(ProfileLookupResponseDto.class)
                    .block();

            if (result == null) {
                throw new ProfileServiceUnavailableException("Profile Service return empty body");
            }

            return result;
        } catch (WebClientException ex) {
            throw new ProfileServiceUnavailableException("Profile Service call failed", ex);
        }
    }

    public ProfileLookupResponseDto findByTelegramUserId(Long telegramUserId) {
        String telegramUserIdString = String.valueOf(telegramUserId);

        try {
            ProfileLookupResponseDto result = webClient.get()
                    .uri(BY_PROFILE)
                    .header(TELEGRAM_ID_HEADER, telegramUserIdString)
                    .retrieve()
                    .onStatus(status -> status.isSameCodeAs(HttpStatus.NOT_FOUND),
                            response -> Mono.error(new ProfileNotFoundException("Profile not found for telegram user id: ".concat(telegramUserIdString))))
                    .bodyToMono(ProfileLookupResponseDto.class)
                    .block();

            if (result == null) {
                throw new ProfileServiceUnavailableException("Profile Service return empty body");
            }

            return new ProfileLookupResponseDto(telegramUserId, result.details());
        } catch (WebClientException ex) {
            throw new ProfileServiceUnavailableException("Profile Service call failed", ex);
        }
    }
}