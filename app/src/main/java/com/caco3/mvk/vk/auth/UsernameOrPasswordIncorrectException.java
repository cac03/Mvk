package com.caco3.mvk.vk.auth;

import com.caco3.mvk.vk.VkException;

public class UsernameOrPasswordIncorrectException extends VkException {

  public UsernameOrPasswordIncorrectException(String message) {
    super(message);
  }

  public UsernameOrPasswordIncorrectException(String message, Throwable cause) {
    super(message, cause);
  }

  public UsernameOrPasswordIncorrectException(Throwable cause) {
    super(cause);
  }
}
