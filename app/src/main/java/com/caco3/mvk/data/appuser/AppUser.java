package com.caco3.mvk.data.appuser;


import com.caco3.mvk.vk.auth.UserToken;

import java.io.Serializable;

public class AppUser implements Serializable {
  private UserToken userToken;
  private String username;

  public AppUser(UserToken userToken, String username) {
    this.userToken = userToken;
    this.username = username;
  }

  public UserToken getUserToken() {
    return userToken;
  }

  public String getUsername() {
    return username;
  }
}
