package com.caco3.mvk.vk;

public abstract class VkException extends RuntimeException {
  public VkException() {
  }

  public VkException(String message) {
    super(message);
  }

  public VkException(String message, Throwable cause) {
    super(message, cause);
  }

  public VkException(Throwable cause) {
    super(cause);
  }
}
