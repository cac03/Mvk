package com.caco3.mvk.vk.auth;

import java.io.Serializable;

public class UserToken implements Serializable {
  private final String accessToken;

  private static final long serialVersionUID = 1241253235L;

  public UserToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getAccessToken() {
    return accessToken;
  }
}
