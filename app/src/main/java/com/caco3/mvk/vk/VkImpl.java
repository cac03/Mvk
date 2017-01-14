package com.caco3.mvk.vk;

import com.caco3.mvk.vk.audio.VkAudioServiceImpl;
import com.caco3.mvk.vk.audio.VkAudiosService;
import com.caco3.mvk.vk.auth.VkAuthService;
import com.caco3.mvk.vk.auth.VkAuthServiceImpl;
import com.caco3.mvk.vk.users.VkUsersService;
import com.caco3.mvk.vk.users.VkUsersServiceImpl;


/*package*/ class VkImpl implements Vk {
  private final VkAuthService vkAuthService = new VkAuthServiceImpl();
  private final VkAudiosService vkAudiosService = new VkAudioServiceImpl();
  private final VkUsersService vkUsersService = new VkUsersServiceImpl();

  @Override
  public VkAuthService auth() {
    return vkAuthService;
  }

  @Override
  public VkAudiosService audios() {
    return vkAudiosService;
  }

  @Override
  public VkUsersService users() {
    return vkUsersService;
  }
}
