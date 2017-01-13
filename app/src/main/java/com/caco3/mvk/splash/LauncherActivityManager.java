package com.caco3.mvk.splash;


import com.caco3.mvk.audios.AudiosActivity;
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
      return AudiosActivity.class;
    } else {
      return LogInActivity.class;
    }
  }
}
