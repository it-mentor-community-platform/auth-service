package com.itmentorcommunityplatform.authservice.internalUser;


import com.itmentorcommunityplatform.authservice.docs.internalUser.GetInternalUsersWithRoles;
import com.itmentorcommunityplatform.authservice.docs.internalUser.InternalUserControllerDocs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class InternalUserController {

    private final InternalUserService internalUserService;


    @PostMapping("/internal/user")
    @InternalUserControllerDocs
    public ResponseEntity<Void> upsertUser(@RequestBody @Valid UserUpsertRequestDto requestDto){
        var result = internalUserService.upsertUser(requestDto);
        return result.created()
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.ok().build();

    }

    @GetMapping("/internal/users")
    @GetInternalUsersWithRoles
    public ResponseEntity<List<UserWithRolesDto>> getListUsers(@RequestParam(value = "telegram_user_ids") List<Long> telegramUserIds){
            var result=internalUserService.getListUsers(telegramUserIds);
            return  ResponseEntity
                    .ok()
                    .body(result);
    }
}
