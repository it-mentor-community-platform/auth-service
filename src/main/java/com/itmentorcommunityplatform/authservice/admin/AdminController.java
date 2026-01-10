package com.itmentorcommunityplatform.authservice.admin;

import com.itmentorcommunityplatform.authservice.docs.admin.UpdateUserRolesDocs;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/user")
    @UpdateUserRolesDocs
    public ResponseEntity<Void> updateUserRoles(
            @RequestParam(name = "telegram_user_id") Long telegramUserId,
            @Valid @RequestBody UpdateUserRolesRequest rolesRequest,
            HttpServletRequest httpRequest
    ) {
        adminService.changeUserRoles(telegramUserId, rolesRequest, httpRequest);

        return ResponseEntity.ok().build();
    }
}
