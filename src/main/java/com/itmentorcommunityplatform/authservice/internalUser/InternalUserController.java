package com.itmentorcommunityplatform.authservice.internalUser;


import com.itmentorcommunityplatform.authservice.docs.internalUser.InternalUserControllerDocs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class InternalUserController {

    private final InternalUserService internalUserService;


    @PostMapping("/internal/user")
    @InternalUserControllerDocs
    public ResponseEntity<Void> upsertUser(@RequestBody @Valid UserUpsertRequestDto requestDto){
        internalUserService.upsertUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
