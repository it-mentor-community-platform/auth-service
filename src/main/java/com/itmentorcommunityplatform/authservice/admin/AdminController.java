package com.itmentorcommunityplatform.authservice.admin;

import com.itmentorcommunityplatform.authservice.docs.admin.UpdateUserRolesDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/user")
    @UpdateUserRolesDocs
    public ResponseEntity<Void> updateUserRoles(
            @RequestParam(name = "telegram_user_id") Long telegramUserId,
            @RequestHeader("X-User-Roles") List<String> headerRoles,
            @RequestBody UpdateUserRolesRequest request
    ) {
        adminService.changeUserRoles(telegramUserId, headerRoles, request);

        return ResponseEntity.ok().build();
    }
}
