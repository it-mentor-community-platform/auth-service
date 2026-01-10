package com.itmentorcommunityplatform.authservice.docs.admin;

import com.itmentorcommunityplatform.authservice.admin.UpdateUserRolesRequest;
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
                
                ### Constraints:
                * **400**: Invalid role name, missing `telegram_user_id`, or wrong ID format.
                * **403**: Requester is not an admin OR attempt to modify ADMIN status.
                * **404**: Target user not found.
                """,
        parameters = {
                @Parameter(
                        name = "telegram_user_id",
                        in = ParameterIn.QUERY,
                        description = "Telegram ID of the user whose roles are being updated",
                        required = true,
                        schema = @Schema(type = "integer", format = "int64"),
                        example = "7634452355"
                ),
                @Parameter(
                        name = "X-User-Roles",
                        in = ParameterIn.HEADER,
                        description = "Roles of the requester (must include ADMIN)",
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
                                value = "{\"roles\": [\"STUDENT\", \"MENTOR\"]}"
                        )
                )
        )
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Roles updated successfully (ADMIN role status remained unchanged)",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Bad Request: Validation failed",
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(name = "Invalid role", value = "{\"message\":\"Invalid role: DOTER\"}"),
                                @ExampleObject(name = "Missing parameter", value = "{\"message\":\"Parameter is missing: telegram_user_id\"}"),
                                @ExampleObject(name = "Invalid ID type", value = "{\"message\":\"Invalid parameter type\"}")
                        }
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden: Access denied or role restriction",
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(name = "Requester not Admin", value = "{\"message\":\"Missing role: ADMIN\"}"),
                                @ExampleObject(name = "Modify ADMIN prohibited", value = "{\"message\":\"Modifying ADMIN role is not allowed\"}")
                        }
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\":\"User not found\"}")
                )
        )
})
public @interface UpdateUserRolesDocs {
}
