package com.caco3.mvk.vk.exceptions;

import com.caco3.mvk.vk.VkError;
import com.caco3.mvk.vk.VkException;


public class AuthFailedException extends VkException {
  public AuthFailedException(VkError vkError) {
    super(vkError);
  }
}