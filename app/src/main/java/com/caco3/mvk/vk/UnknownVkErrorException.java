package com.caco3.mvk.vk;

public class UnknownVkErrorException extends VkException {
  public UnknownVkErrorException(VkError vkError) {
    super(vkError);
  }

  public UnknownVkErrorException(String message) {
    super(message);
  }
}
