package com.caco3.mvk.vk.users;


import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class VkUser {
  @Id
  private Long id;

  @SerializedName("uid")
  private long vkUserId;
  @SerializedName("first_name")
  private String firstName;
  @SerializedName("last_name")
  private String lastName;
  @SerializedName("photo_200_orig")
  private String photoUrl;
  private long lastUpdated;

  @Generated(hash = 707624543)
  public VkUser(Long id, long vkUserId, String firstName, String lastName,
                String photoUrl, long lastUpdated) {
    this.id = id;
    this.vkUserId = vkUserId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.photoUrl = photoUrl;
    this.lastUpdated = lastUpdated;
  }

  @Generated(hash = 1074779495)
  public VkUser() {
  }

  public long getVkUserId() {
    return vkUserId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setVkUserId(long vkUserId) {
    this.vkUserId = vkUserId;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public long getLastUpdated() {
    return this.lastUpdated;
  }

  public void setLastUpdated(long lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
