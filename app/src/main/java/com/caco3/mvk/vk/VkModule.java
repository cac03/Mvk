package com.caco3.mvk.vk;

import com.caco3.mvk.loggedin.LoggedInScope;
import com.caco3.mvk.vk.auth.UserToken;
import com.google.gson.Gson;


import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;


@Module
public class VkModule {
  @Provides
  @LoggedInScope
  public Vk provideVk(UserToken userToken, OkHttpClient okHttpClient, Gson gson) {
    return new VkImpl(userToken, okHttpClient, gson);
  }
}
