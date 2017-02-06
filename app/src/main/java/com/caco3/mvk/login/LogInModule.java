package com.caco3.mvk.login;

import com.caco3.mvk.data.appuser.AppUsersRepository;
import com.caco3.mvk.data.usertoken.UserTokenRepository;
import com.caco3.mvk.vk.auth.VkAuthService;

import dagger.Module;
import dagger.Provides;

@LogInScope
@Module
public class LogInModule {
  @Provides
  @LogInScope
  public LogInPresenter provideLogInPresenter(VkAuthService vkAuthService, AppUsersRepository appUsersRepository,
                                              UserTokenRepository userTokenRepository) {
    return new LogInPresenterImpl(vkAuthService, appUsersRepository, userTokenRepository);
  }
}
