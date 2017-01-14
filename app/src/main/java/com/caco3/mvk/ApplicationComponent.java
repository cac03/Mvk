package com.caco3.mvk;

import com.caco3.mvk.data.DataModule;
import com.caco3.mvk.loggedin.LoggedInComponent;
import com.caco3.mvk.loggedin.LoggedInModule;
import com.caco3.mvk.login.LogInComponent;
import com.caco3.mvk.login.LogInModule;
import com.caco3.mvk.network.NetworkModule;
import com.caco3.mvk.splash.SplashComponent;
import com.caco3.mvk.splash.SplashModule;
import com.caco3.mvk.vk.VkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                DataModule.class,
                NetworkModule.class,
                VkModule.class
        }
)
public interface ApplicationComponent {
  LogInComponent plus(LogInModule logInModule);
  SplashComponent plus(SplashModule splashModule);
  LoggedInComponent plus(LoggedInModule loggedInModule);
}
