package com.itmentorcommunityplatform.authservice.dummyAuth;

import jakarta.validation.constraints.NotNull;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(@NotNull Integer userId) {
    super("User with id = " + userId + " not found");
  }
}
