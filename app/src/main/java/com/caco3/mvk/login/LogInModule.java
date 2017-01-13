package com.caco3.mvk.login;

import com.caco3.mvk.data.appuser.AppUsersRepository;
import com.caco3.mvk.data.usertoken.UserTokenRepository;
import com.caco3.mvk.vk.Vk;

import dagger.Module;
import dagger.Provides;

@LogInScope
@Module
public class LogInModule {
  @Provides
  @LogInScope
  public LogInPresenter provideLogInPresenter(Vk vk, AppUsersRepository appUsersRepository,
                                              UserTokenRepository userTokenRepository) {
    return new LogInPresenterImpl(vk, appUsersRepository, userTokenRepository);
  }
}
