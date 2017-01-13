package com.caco3.mvk.vk;

import com.google.gson.annotations.SerializedName;

import static com.caco3.mvk.util.Preconditions.checkState;

public abstract class VkMethodResponse implements VkResponse {
  @SerializedName("error") public VkError error;

  @Override
  public boolean isSuccessful() {
    return error == null;
  }

  @Override
  public VkException getException() {
    checkState(isSuccessful(), "attempt to get an exception, but response is successful");
    // TODO: 1/2/17 handle global error
    return new VkException() {
    };
  }
}