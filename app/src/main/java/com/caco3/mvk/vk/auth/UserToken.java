package com.caco3.mvk.vk.auth;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserToken implements Serializable {
  @Id
  private Long id;
  @NotNull
  private String accessToken;
  @NotNull
  @SerializedName("user_id")
  private long vkUserId;

  private static final long serialVersionUID = 1241253235L;

  @Keep
  public UserToken(String accessToken) {
    this.accessToken = accessToken;
  }

  @Generated(hash = 57771346)
  public UserToken(Long id, @NotNull String accessToken, long vkUserId) {
    this.id = id;
    this.accessToken = accessToken;
    this.vkUserId = vkUserId;
  }

  @Generated(hash = 2113443620)
  public UserToken() {
  }

  public String getAccessToken() {
    return accessToken;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public long getVkUserId() {
    return this.vkUserId;
  }

  public void setVkUserId(long vkUserId) {
    this.vkUserId = vkUserId;
  }
}
