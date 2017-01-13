package com.caco3.mvk.vk.auth;

import java.io.IOException;


public interface VkAuthService {
  UserToken getUserToken(Credentials credentials) throws IOException;
}
