package com.itmentorcommunityplatform.authservice.dummyAuth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Schema(description = "Request DTO for dummy authentication. Exactly one of telegram_user_id, telegram_url, github_profile_url must be provided")
public record DummyRequestDto(

        @JsonProperty("telegram_user_id")
        @Positive(message = "telegram id must be positive")
        @Schema(example = "123456789")
        Long telegramUserId,

        @JsonProperty("telegram_url")
        @Pattern(regexp = ".*\\S.*", message = "telegram url must not be blank")
        @Schema(example = "https://t.me/zhukovsd")
        String telegramUrl,

        @JsonProperty("github_profile_url")
        @Pattern(regexp = ".*\\S.*", message = "github profile url must not be blank")
        @Schema(example = "https://github.com/zhukovsd")
        String githubProfileUrl,

        @JsonProperty("roles")
        @NotEmpty(message = "roles must not be empty")
        List<String> rolesNames) {

    @JsonIgnore
    @Schema(hidden = true)
    @AssertTrue(message = "Exactly one of telegram_user_id, telegram_url, github_profile_url must be provided")
    public boolean isExactlyOneIdentifierProvided() {
        return Stream.of(telegramUserId, telegramUrl, githubProfileUrl)
                .filter(Objects::nonNull)
                .count() == 1;
    }
}
