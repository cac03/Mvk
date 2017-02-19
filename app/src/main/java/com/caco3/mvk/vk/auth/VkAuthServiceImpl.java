package com.caco3.mvk.vk.auth;

import java.io.IOException;


public class VkAuthServiceImpl implements VkAuthService {

  @Override
  public UserToken getUserToken(Credentials credentials) throws IOException {
    return new AuthorizeMethod(credentials).call();
  }
}
