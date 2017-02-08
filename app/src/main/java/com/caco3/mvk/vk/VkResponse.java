package com.caco3.mvk.vk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VkResponse<T> {
  @SerializedName("error")
  @Expose
  private VkError vkError;
  @SerializedName("response")
  @Expose
  private T response;

  public VkError getVkError() {
    return vkError;
  }

  public T getResponseOrThrowIfNotSuccessful() {
    if (isSuccessful()) {
      return response;
    } else {
      throw vkError.propagate();
    }
  }

  private boolean isSuccessful() {
    return vkError == null;
  }
}