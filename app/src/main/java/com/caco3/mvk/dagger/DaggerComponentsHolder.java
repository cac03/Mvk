package com.caco3.mvk.dagger;

import android.content.Context;

import com.caco3.mvk.ApplicationComponent;
import com.caco3.mvk.ApplicationModule;
import com.caco3.mvk.DaggerApplicationComponent;
import com.caco3.mvk.myaudios.MyAudiosComponent;
import com.caco3.mvk.myaudios.MyAudiosModule;
import com.caco3.mvk.data.DataModule;
import com.caco3.mvk.loggedin.LoggedInComponent;
import com.caco3.mvk.loggedin.LoggedInModule;
import com.caco3.mvk.login.LogInComponent;
import com.caco3.mvk.login.LogInModule;
import com.caco3.mvk.navdrawer.NavDrawerComponent;
import com.caco3.mvk.navdrawer.NavDrawerModule;
import com.caco3.mvk.splash.SplashComponent;
import com.caco3.mvk.splash.SplashModule;

import timber.log.Timber;

import static com.caco3.mvk.util.Preconditions.checkState;

public class DaggerComponentsHolder {
  private static final DaggerComponentsHolder INSTANCE = new DaggerComponentsHolder();
  private ApplicationComponent applicationComponent;
  private LogInComponent logInComponent;
  private SplashComponent splashComponent;
  private LoggedInComponent loggedInComponent;
  private MyAudiosComponent myAudiosComponent;
  private NavDrawerComponent navDrawerComponent;

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

  public LogInComponent getLogInComponent() {
    if (logInComponent == null) {
      logInComponent = applicationComponent.plus(new LogInModule());
    }
    return logInComponent;
  }

  public void releaseLogInComponent() {
    logInComponent = null;
  }

  public SplashComponent getSplashComponent() {
    if (splashComponent == null) {
      splashComponent = applicationComponent.plus(new SplashModule());
    }
    return splashComponent;
  }

  public void releaseSplashComponent() {
    splashComponent = null;
  }

  public LoggedInComponent getLoggedInComponent() {
    if (loggedInComponent == null) {
      loggedInComponent = applicationComponent.plus(new LoggedInModule());
    }
    return loggedInComponent;
  }

  public void releaseLoggedInComponent() {
    loggedInComponent = null;
  }

  public MyAudiosComponent getMyAudiosComponent() {
    if (myAudiosComponent == null) {
      if (loggedInComponent == null) {
        loggedInComponent = applicationComponent.plus(new LoggedInModule());
      }
      myAudiosComponent = loggedInComponent.plus(new MyAudiosModule());
    }
    return myAudiosComponent;
  }

  public void releaseMyAudiosComponent() {
    myAudiosComponent = null;
  }

  public NavDrawerComponent getNavDrawerComponent() {
    if (navDrawerComponent == null) {
      if (loggedInComponent == null) {
        loggedInComponent = applicationComponent.plus(new LoggedInModule());
      }
      navDrawerComponent = loggedInComponent.plus(new NavDrawerModule());
    }
    return navDrawerComponent;
  }

  public void releaseNavDrawerComponent() {
    navDrawerComponent = null;
  }

  public ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }
}
