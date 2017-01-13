package com.caco3.mvk.vk;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class VkModule {
  @Provides
  @Singleton
  public Vk provideVk() {
    return new VkImpl();
  }
}
