package com.caco3.mvk.vk.auth;

import java.io.Serializable;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class Credentials implements Serializable {
  private final String username;
  private final String password;

  private static final long serialVersionUID = -4524633346787979L;

  public Credentials(String username, String password) {
    this.username = checkNotNull(username, "username == null");
    this.password = checkNotNull(password, "password == null");
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
