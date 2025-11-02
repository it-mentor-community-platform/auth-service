package com.itmentorcommunityplatform.authservice.mapper;


import com.itmentorcommunityplatform.authservice.auth.User;
import com.itmentorcommunityplatform.authservice.auth.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
}
