package com.caco3.mvk;

import com.caco3.mvk.boot.BootBroadcastReceiver;
import com.caco3.mvk.boot.BootModule;
import com.caco3.mvk.data.DataModule;
import com.caco3.mvk.loggedin.LoggedInComponent;
import com.caco3.mvk.loggedin.LoggedInModule;
import com.caco3.mvk.login.LogInComponent;
import com.caco3.mvk.login.LogInModule;
import com.caco3.mvk.network.NetworkModule;
import com.caco3.mvk.settings.SettingsFragment;
import com.caco3.mvk.settings.SettingsModule;
import com.caco3.mvk.splash.SplashComponent;
import com.caco3.mvk.splash.SplashModule;
import com.caco3.mvk.storage.StorageModule;
import com.caco3.mvk.vk.VkAuthModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                BootModule.class,
                DataModule.class,
                NetworkModule.class,
                StorageModule.class,
                SettingsModule.class,
                VkAuthModule.class
        }
)
public interface ApplicationComponent {
  LogInComponent plus(LogInModule logInModule);
  SplashComponent plus(SplashModule splashModule);
  LoggedInComponent plus(LoggedInModule loggedInModule);
  void inject(SettingsFragment settingsFragment);
  void inject(BootBroadcastReceiver receiver);
}
