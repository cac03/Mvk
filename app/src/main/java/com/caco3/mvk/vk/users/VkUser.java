package com.caco3.mvk.vk.users;


import com.google.gson.annotations.SerializedName;

public class VkUser {
  @SerializedName("id")
  private long vkUserId;
  @SerializedName("first_name")
  private String firstName;
  @SerializedName("last_name")
  private String lastName;
  @SerializedName("photo_200_orig")
  private String photoUrl;

}
