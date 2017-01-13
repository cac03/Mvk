package com.caco3.mvk.vk;

import com.caco3.mvk.vk.audio.VkAudioServiceImpl;
import com.caco3.mvk.vk.audio.VkAudiosService;
import com.caco3.mvk.vk.auth.VkAuthService;
import com.caco3.mvk.vk.auth.VkAuthServiceImpl;


/*package*/ class VkImpl implements Vk {
  private final VkAuthService vkAuthService = new VkAuthServiceImpl();
  private final VkAudiosService vkAudiosService = new VkAudioServiceImpl();

  @Override
  public VkAuthService auth() {
    return vkAuthService;
  }

  @Override
  public VkAudiosService audios() {
    return vkAudiosService;
  }
}
