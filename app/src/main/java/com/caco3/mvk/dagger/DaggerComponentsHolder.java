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

  public boolean hasMyAudiosComponent() {
    return myAudiosComponent != null;
  }

  public MyAudiosComponent getMyAudiosComponent() {
    return myAudiosComponent;
  }

  public void createMyAudiosComponent() {
    checkState(loggedInComponent != null,
            "Attempt to create MyAudiosComponent, but LoggedInComponent was not created");
    myAudiosComponent = loggedInComponent.plus(new MyAudiosModule());
  }

  public void releaseMyAudiosComponent() {
    myAudiosComponent = null;
  }

  public boolean hasNavDrawerComponent() {
    return navDrawerComponent != null;
  }

  public NavDrawerComponent getNavDrawerComponent() {
    return navDrawerComponent;
  }

  public void createNavDrawerComponent() {
    checkState(loggedInComponent != null,
            "Attempt to create NavDrawerComponent, but LoggedInComponent was not created");
    navDrawerComponent = loggedInComponent.plus(new NavDrawerModule());
  }

  public void releaseNavDrawerComponent() {
    navDrawerComponent = null;
  }
}
