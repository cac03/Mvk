package com.caco3.mvk.vk;

public interface VkResponse {
  boolean isSuccessful();
  VkException getException();
}
