package com.caco3.mvk.vk.users;

import java.io.IOException;


public interface VkUsersService {
  /**
   * Returns {@link VkUser} object corresponding to current user
   */
  VkUser get() throws IOException;
}