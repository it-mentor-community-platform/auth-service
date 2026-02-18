package com.itmentorcommunityplatform.authservice.mapper;


import com.itmentorcommunityplatform.authservice.auth.UserResponseDto;
import com.itmentorcommunityplatform.authservice.auth.UserRolesDto;
import com.itmentorcommunityplatform.authservice.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);

    UserRolesDto toUserRolesDto(User user, List<String> roles);

    UserResponseDto toResponseDto(UserRolesDto userRolesDto);
}
