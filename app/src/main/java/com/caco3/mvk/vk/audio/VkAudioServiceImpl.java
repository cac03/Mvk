package com.caco3.mvk.vk.audio;

import com.caco3.mvk.vk.auth.UserToken;

import java.io.IOException;
import java.util.List;

public class VkAudioServiceImpl implements VkAudiosService {
  @Override
  public List<Audio> get(UserToken userToken) throws IOException {
    return new AudioGetMethod(userToken).call();
  }
}
