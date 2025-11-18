package com.itmentorcommunityplatform.authservice.docs.dummyAuth;


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
        summary = "Authenticate user via Dummy method",
        description = """
                ### How to test this endpoint

                For testing this dummy authentication, use any valid `userId` that exists in your local/test database.  
                Replace the `userId` in the example below with a real value.
                """,
        requestBody = @RequestBody(
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(
                                        name = "DummyRequestDto example",
                                        value = """
                                        {
                                          "user_id": 1,
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
                responseCode = "200",
                description = "Successful request. JWT returned in Authorization header."
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Resource not found",
                content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
        )
})
public @interface DummyAuthControllerDocs {
}
