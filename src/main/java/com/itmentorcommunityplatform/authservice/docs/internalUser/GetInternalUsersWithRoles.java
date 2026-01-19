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
                        schema = @Schema(example = """
                                {
                                  "timestamp": "2026-01-18T21:41:42.704234900Z",
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "telegram_user_ids must not contain nulls",
                                  "path": "/api/auth/internal/users"
                                }
                                """)
                )),
        @ApiResponse(
                responseCode = "400",
                description = "Telegram_user_ids must not be empty",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                                {
                                  "timestamp": "2026-01-18T21:42:42.722176100Z",
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "telegram_user_ids must not be empty",
                                  "path": "/api/auth/internal/users"
                                }
                                """)
                )),
        @ApiResponse(
                responseCode = "400",
                description = "Telegram_user_ids must be positive",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                                {
                                  "timestamp": "2026-01-18T21:43:08.427385700Z",
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "telegram_user_ids must be positive",
                                  "path": "/api/auth/internal/users"
                                }
                                """)
                )),
        @ApiResponse(
                responseCode = "400",
                description = "Parameter is missing: telegram_user_ids",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                                {
                                  "timestamp": "2026-01-18T21:43:29.021364400Z",
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "Parameter is missing: telegram_user_ids",
                                  "path": "/api/auth/internal/users"
                                }
                                """)
                )),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid parameter type",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = """
                                {
                                  "timestamp": "2026-01-18T21:43:52.016065300Z",
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "Invalid parameter type",
                                  "path": "/api/auth/internal/users"
                                }
                                """)
                ))
}
)
public @interface GetInternalUsersWithRoles {
}
