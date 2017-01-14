package com.caco3.mvk.network;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {
  @Provides
  @Singleton
  public NetworkManager provideNetworkManager(Context context) {
    return new NetworkManagerImpl(context);
  }
}
