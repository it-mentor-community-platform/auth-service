package com.itmentorcommunityplatform.authservice.docs.admin;

import com.itmentorcommunityplatform.authservice.admin.UpdateUserRolesRequest;
import com.itmentorcommunityplatform.authservice.advice.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
        summary = "Update user roles (Admin only)",
        description = """
                Endpoint for managing user roles. 
                
                **Rules:**
                1. Only users with header `X-User-Roles: ADMIN` can access this.
                2. It is **prohibited** to add or remove the `ADMIN` role.
                3. If the target user is already an ADMIN, their ADMIN role will be preserved, but other roles can be updated.
                """,
        parameters = {
                @Parameter(
                        name = "telegram_user_id",
                        in = ParameterIn.QUERY,
                        required = true,
                        schema = @Schema(type = "integer", format = "int64"),
                        example = "7634452355"
                ),
                @Parameter(
                        name = "X-User-Roles",
                        in = ParameterIn.HEADER,
                        required = true,
                        schema = @Schema(type = "string"),
                        example = "ADMIN"
                )
        },
        requestBody = @RequestBody(
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UpdateUserRolesRequest.class),
                        examples = @ExampleObject(
                                name = "Standard role update",
                                value = """
                                        {
                                          "roles": ["STUDENT", "MENTOR"]
                                        }
                                        """
                        )
                )
        )
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Roles updated successfully",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Bad Request: Validation failed",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "Invalid role",
                                        value = """
                                                {
                                                  "timestamp": "2026-01-10T16:42:47.629Z",
                                                  "status": 400,
                                                  "error": "Bad Request",
                                                  "message": "Invalid role: DOTER",
                                                  "path": "/api/auth/admin/user"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "Missing parameter",
                                        value = """
                                                {
                                                  "timestamp": "2026-01-10T16:42:47.629Z",
                                                  "status": 400,
                                                  "error": "Bad Request",
                                                  "message": "Parameter is missing: telegram_user_id",
                                                  "path": "/api/auth/admin/user"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "Invalid ID type",
                                        value = """
                                                {
                                                  "timestamp": "2026-01-10T16:42:47.629Z",
                                                  "status": 400,
                                                  "error": "Bad Request",
                                                  "message": "Invalid parameter type",
                                                  "path": "/api/auth/admin/user"
                                                }
                                                """
                                )
                        }
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden: Access denied or role restriction",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "Requester not Admin",
                                        value = """
                                                {
                                                  "timestamp": "2026-01-10T16:42:47.629Z",
                                                  "status": 403,
                                                  "error": "Forbidden",
                                                  "message": "Missing role: ADMIN",
                                                  "path": "/api/auth/admin/user"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "Modify ADMIN prohibited",
                                        value = """
                                                {
                                                  "timestamp": "2026-01-10T16:42:47.629Z",
                                                  "status": 403,
                                                  "error": "Forbidden",
                                                  "message": "Modifying ADMIN role is not allowed",
                                                  "path": "/api/auth/admin/user"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "Header missing",
                                        value = """
                                                {
                                                  "timestamp": "2026-01-10T16:42:47.629Z",
                                                  "status": 403,
                                                  "error": "Forbidden",
                                                  "message": "Required header 'X-User-Roles' is missing",
                                                  "path": "/api/auth/admin/user"
                                                }
                                                """
                                )
                        }
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponseDto.class),
                        examples = @ExampleObject(
                                name = "User not found",
                                value = """
                                        {
                                          "timestamp": "2026-01-10T16:42:47.629Z",
                                          "status": 404,
                                          "error": "Not Found",
                                          "message": "User not found",
                                          "path": "/api/auth/admin/user"
                                        }
                                        """
                        )
                )
        )
})
public @interface UpdateUserRolesDocs {
}