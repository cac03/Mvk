package com.caco3.mvk.vk.users;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class VkUser implements Cloneable {
  @Id
  private Long id;

  @SerializedName("uid")
  @Expose
  private long vkUserId;
  @SerializedName("first_name")
  @Expose
  private String firstName;
  @SerializedName("last_name")
  @Expose
  private String lastName;
  @SerializedName("photo_200_orig")
  @Expose
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    VkUser vkUser = (VkUser) o;

    if (vkUserId != vkUser.vkUserId) return false;
    if (lastUpdated != vkUser.lastUpdated) return false;
    if (id != null ? !id.equals(vkUser.id) : vkUser.id != null) return false;
    if (firstName != null ? !firstName.equals(vkUser.firstName) : vkUser.firstName != null)
      return false;
    if (lastName != null ? !lastName.equals(vkUser.lastName) : vkUser.lastName != null)
      return false;
    return photoUrl != null ? photoUrl.equals(vkUser.photoUrl) : vkUser.photoUrl == null;

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (int) (vkUserId ^ (vkUserId >>> 32));
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
    result = 31 * result + (int) (lastUpdated ^ (lastUpdated >>> 32));
    return result;
  }

  @Override
  public VkUser clone() {
    try {
      return (VkUser) super.clone();
    } catch (CloneNotSupportedException cannotHappen) {
      throw new AssertionError("We're cloneable. This cannot happen.");
    }
  }
}
