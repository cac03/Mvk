package com.caco3.mvk.vk.exceptions;

import com.caco3.mvk.vk.VkError;
import com.caco3.mvk.vk.VkException;


public class AppDisabledException extends VkException {
  public AppDisabledException(VkError vkError) {
    super(vkError);
  }
}
