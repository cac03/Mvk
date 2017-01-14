package com.caco3.mvk.vk.users;

import com.caco3.mvk.vk.auth.UserToken;

import java.io.IOException;


public class VkUsersServiceImpl implements VkUsersService {
  @Override
  public VkUser get(UserToken userToken) throws IOException {
    return new UserGetMethod(userToken).call();
  }
}
