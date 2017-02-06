package com.caco3.mvk.vk;

import com.caco3.mvk.vk.auth.VkAuthService;
import com.caco3.mvk.vk.auth.VkAuthServiceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class VkAuthModule {
  @Provides
  @Singleton
  public VkAuthService provideVkAuthService() {
    return new VkAuthServiceImpl();
  }
}
