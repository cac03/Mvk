package com.caco3.mvk.splash;

import com.caco3.mvk.data.appuser.AppUsersRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashModule {

  @Provides
  @SplashScope
  public LauncherActivityManager provideLauncherActivityManager(AppUsersRepository appUsersRepository) {
    return new LauncherActivityManager(appUsersRepository);
  }
}
