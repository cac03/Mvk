package com.caco3.mvk.vk.exceptions;

import com.caco3.mvk.vk.VkError;
import com.caco3.mvk.vk.VkException;


public class CaptchaNeededException extends VkException {
  public CaptchaNeededException(VkError vkError) {
    super(vkError);
  }
}
