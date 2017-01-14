package com.caco3.mvk.vk.users;

import com.caco3.mvk.vk.auth.UserToken;

import java.io.IOException;


public interface VkUsersService {
  /**
   * Returns {@link VkUser} object corresponding to current user
   */
  VkUser get(UserToken userToken) throws IOException;
}