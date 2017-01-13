package com.caco3.mvk.vk;


import com.caco3.mvk.vk.audio.VkAudiosService;
import com.caco3.mvk.vk.auth.VkAuthService;

public interface Vk {
  VkAudiosService audios();
  VkAuthService auth();
}
