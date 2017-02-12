package com.caco3.mvk.vk.users;

import java.io.IOException;
import java.util.List;


public interface VkUsersService {
  /**
   * Returns {@link VkUser} object corresponding to current user
   */
  VkUser get() throws IOException;
  List<VkUser> get(long... ids) throws IOException;
}