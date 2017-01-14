package com.caco3.mvk.dagger;

import android.content.Context;

import com.caco3.mvk.ApplicationComponent;
import com.caco3.mvk.ApplicationModule;
import com.caco3.mvk.DaggerApplicationComponent;
import com.caco3.mvk.data.DataModule;
import com.caco3.mvk.loggedin.LoggedInComponent;
import com.caco3.mvk.loggedin.LoggedInModule;
import com.caco3.mvk.login.LogInComponent;
import com.caco3.mvk.login.LogInModule;
import com.caco3.mvk.splash.SplashComponent;
import com.caco3.mvk.splash.SplashModule;

import timber.log.Timber;

public class DaggerComponentsHolder {
  private static final DaggerComponentsHolder INSTANCE = new DaggerComponentsHolder();
  private ApplicationComponent applicationComponent;
  private LogInComponent logInComponent;
  private SplashComponent splashComponent;
  private LoggedInComponent loggedInComponent;

  public static DaggerComponentsHolder getInstance() {
    return INSTANCE;
  }

  private DaggerComponentsHolder(){
  }

  public void initApplicationComponent(Context context) {
    if (applicationComponent != null) {
      Timber.w("Application component is already initialized");
    }
    applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(context))
            .dataModule(new DataModule(context))
            .build();
  }

  public boolean hasLogInComponent() {
    return logInComponent != null;
  }

  public LogInComponent getLogInComponent() {
    return logInComponent;
  }

  public void createLogInComponent() {
    logInComponent = applicationComponent.plus(new LogInModule());
  }

  public void releaseLogInComponent() {
    logInComponent = null;
  }

  public boolean hasSplashComponent() {
    return splashComponent != null;
  }

  public SplashComponent getSplashComponent() {
    return splashComponent;
  }

  public void createSplashComponent() {
    splashComponent = applicationComponent.plus(new SplashModule());
  }

  public void releaseSplashComponent() {
    splashComponent = null;
  }

  public boolean hasLoggedInComponent() {
    return loggedInComponent != null;
  }

  public LoggedInComponent getLoggedInComponent() {
    return loggedInComponent;
  }

  public void createLoggedInComponent() {
    loggedInComponent = applicationComponent.plus(new LoggedInModule());
  }

  public void releaseLoggedInComponent() {
    loggedInComponent = null;
  }
}
