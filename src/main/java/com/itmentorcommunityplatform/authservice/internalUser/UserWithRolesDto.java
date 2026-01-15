package com.itmentorcommunityplatform.authservice.internalUser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonPropertyOrder({"telegram_user_id", "roles"})
public class UserWithRolesDto {

    @JsonProperty("telegram_user_id")
    Long telegramUserId;

    List<String> roles;

}
