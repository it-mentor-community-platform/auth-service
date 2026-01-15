package com.itmentorcommunityplatform.authservice.docs.internalUser;


import com.itmentorcommunityplatform.authservice.internalUser.UserWithRolesDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Get users with roles name",
        description = "Returns the telegramUserId with roles name"
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Returns the telegramUserId with roles name",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserWithRolesDto.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Telegram_user_ids must not contain nulls",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\n" +
                                "  \"timestamp\": \"2026-01-15T20:15:31.729733400Z\",\n" +
                                "  \"status\": 400,\n" +
                                "  \"error\": \"Bad Request\",\n" +
                                "  \"message\": \"telegram_user_ids must not contain nulls\",\n" +
                                "  \"path\": \"/api/auth/internal/users\"\n" +
                                "}")
                )),
        @ApiResponse(
                responseCode = "400",
                description = "Telegram_user_ids must not be empty",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\n" +
                                "  \"timestamp\": \"2026-01-15T20:16:01.271706700Z\",\n" +
                                "  \"status\": 400,\n" +
                                "  \"error\": \"Bad Request\",\n" +
                                "  \"message\": \"telegram_user_ids must not be empty\",\n" +
                                "  \"path\": \"/api/auth/internal/users\"\n" +
                                "}")
                )),
        @ApiResponse(
                responseCode = "400",
                description = "Telegram_user_ids must be positive",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\n" +
                                "  \"timestamp\": \"2026-01-15T20:16:27.825148800Z\",\n" +
                                "  \"status\": 400,\n" +
                                "  \"error\": \"Bad Request\",\n" +
                                "  \"message\": \"telegram_user_ids must be positive\",\n" +
                                "  \"path\": \"/api/auth/internal/users\"\n" +
                                "}")
                )),
        @ApiResponse(
                responseCode = "400",
                description = "Parameter is missing: telegram_user_ids",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\n" +
                                "  \"timestamp\": \"2026-01-15T20:16:59.682342900Z\",\n" +
                                "  \"status\": 400,\n" +
                                "  \"error\": \"Bad Request\",\n" +
                                "  \"message\": \"Parameter is missing: telegram_user_ids\",\n" +
                                "  \"path\": \"/api/auth/internal/users\"\n" +
                                "}")
                )),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid parameter type",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\n" +
                                "  \"timestamp\": \"2026-01-15T20:24:16.694916700Z\",\n" +
                                "  \"status\": 400,\n" +
                                "  \"error\": \"Bad Request\",\n" +
                                "  \"message\": \"Invalid parameter type\",\n" +
                                "  \"path\": \"/api/auth/internal/users\"\n" +
                                "}")
                ))
}
)
public @interface GetInternalUsersWithRoles {
}
