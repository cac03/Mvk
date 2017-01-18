package com.caco3.mvk.splash;


import com.caco3.mvk.myaudios.MyAudiosActivity;
import com.caco3.mvk.dagger.DaggerComponentsHolder;
import com.caco3.mvk.data.appuser.AppUsersRepository;
import com.caco3.mvk.login.LogInActivity;
import com.caco3.mvk.ui.BaseActivity;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

/**
 * Decides which activity to run after splash screen
 */
public class LauncherActivityManager {
  private final AppUsersRepository appUsersRepository;

  public LauncherActivityManager(AppUsersRepository appUsersRepository) {
    this.appUsersRepository = checkNotNull(appUsersRepository);
  }

  public Class<? extends BaseActivity> getLauncherActivity() {
    if (appUsersRepository.hasActiveAppUser()) {
      putLoggedInComponent();
      return MyAudiosActivity.class;
    } else {
      return LogInActivity.class;
    }
  }

  private void putLoggedInComponent() {
    DaggerComponentsHolder componentsHolder = DaggerComponentsHolder.getInstance();
    if (!componentsHolder.hasLoggedInComponent()) {
      componentsHolder.createLoggedInComponent();
    }
  }
}
