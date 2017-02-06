package com.caco3.mvk.vk;

import com.caco3.mvk.vk.auth.UserToken;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;


@Module
public class VkModule {
  @Provides
  @Singleton
  public Vk provideVk(UserToken userToken, OkHttpClient okHttpClient, Gson gson) {
    return new VkImpl(userToken, okHttpClient, gson);
  }
}
