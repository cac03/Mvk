package com.caco3.mvk.navdrawer;

import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.appuser.AppUsersRepository;
import com.caco3.mvk.data.vkuser.VkUsersRepository;
import com.caco3.mvk.loggedin.LoggedInScope;
import com.caco3.mvk.vk.Vk;

import dagger.Module;
import dagger.Provides;

@Module
public class NavDrawerModule {
  @Provides
  @NavDrawerScope
  /*package*/ NavDrawerPresenter provideNavDrawerPresenter(AppUsersRepository appUsersRepository,
                                                      AppUser appUser,
                                                      VkUsersRepository vkUsersRepository, Vk vk){
    return new NavDrawerPresenterImpl(appUsersRepository, appUser, vkUsersRepository, vk);
  }
}
