package com.caco3.mvk.vk;

public class UnknownVkErrorException extends VkException {
  public UnknownVkErrorException() {
    super();
  }

  public UnknownVkErrorException(String message) {
    super(message);
  }

  public UnknownVkErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnknownVkErrorException(Throwable cause) {
    super(cause);
  }
}
