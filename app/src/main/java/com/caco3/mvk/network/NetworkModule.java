package com.caco3.mvk.network;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class NetworkModule {
  @Provides
  @Singleton
  public NetworkManager provideNetworkManager(Context context) {
    return new NetworkManagerImpl(context);
  }

  @Provides
  @Singleton
  public OkHttpClient provideHttpClient() {
    return new OkHttpClient();
  }
}
