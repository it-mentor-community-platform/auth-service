package com.itmentorcommunityplatform.authservice.docs.internalUser;

import com.itmentorcommunityplatform.authservice.advice.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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
        summary = "Import internal user with roles",
        description = """
                ### How to test this endpoint
                
                For testing this internal user import, provide a new `telegram_user_id` and assign roles.  
                Replace the values below with valid data.
                """,
        requestBody = @RequestBody(
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(
                                        name = "InternalUserRequestDto example",
                                        value = """
                                        {
                                          "telegram_user_id": 6440401305,
                                          "roles": ["ADMIN","STUDENT"]
                                        }
                                        """
                                )
                        }
                )
        )
)
@ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "User imported successfully."
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation error or unknown role",
                content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
        )
})
public @interface InternalUserControllerDocs {
}
