package com.itmentorcommunityplatform.authservice.docs.dummyAuth;


import com.itmentorcommunityplatform.authservice.advice.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Authenticate user via Dummy method",
        description = """
                ### How to test this endpoint
                
                Available only in non-production environments (not available with the `prod` profile).
                
                Provide **exactly one** of the following identifiers and a list of roles:
                
                - `telegram_user_id` - the user is searched in the auth-service database
                - `telegram_url` - the profile is searched by Telegram link in Profile Service
                - `github_profile_url` - the profile is searched by GitHub link in Profile Service
                
                The link format is validated by Profile Service, its 400 response is returned as 400.
                
                ### Notes
                
                - Link search is **case-sensitive**: use the link exactly as it is stored in the profile.
                - The `telegram_username` claim in the JWT is taken from the profile's `telegram_url`.
                  If the profile has no `telegram_url`, the claim is absent from the token.
                """,
        requestBody = @RequestBody(
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(
                                        name = "By telegram_user_id",
                                        summary = "Find user by Telegram user id",
                                        value = """
                                                {
                                                  "telegram_user_id": 395212293,
                                                  "roles": ["ADMIN", "STUDENT"]
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "By telegram_url",
                                        summary = "Find user by Telegram profile link",
                                        value = """
                                                {
                                                  "telegram_url": "https://t.me/krios2146",
                                                  "roles": ["ADMIN", "STUDENT"]
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "By github_profile_url",
                                        summary = "Find user by GitHub profile link",
                                        value = """
                                                {
                                                  "github_profile_url": "https://github.com/krios2146",
                                                  "roles": ["ADMIN", "STUDENT"]
                                                }
                                                """
                                )
                        }
                )
        )
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Successful request. JWT returned in the X-Access-Token header, response body is empty.",
                headers = @Header(
                        name = "X-Access-Token",
                        description = "JWT access token",
                        schema = @Schema(type = "string")
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = """
                        Validation error: none or more than one identifier provided, empty roles, \
                        unknown role, or an incorrect link (rejected by Profile Service)""",
                content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "User not found, or profile not found in Profile Service for the given link",
                content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error, including Profile Service being unavailable",
                content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
        )
})
public @interface DummyAuthControllerDocs {
}