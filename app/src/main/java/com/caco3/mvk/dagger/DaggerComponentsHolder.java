package com.caco3.mvk.dagger;

import android.content.Context;

import com.caco3.mvk.ApplicationComponent;
import com.caco3.mvk.ApplicationModule;
import com.caco3.mvk.DaggerApplicationComponent;
import com.caco3.mvk.data.DataModule;
import com.caco3.mvk.login.LogInComponent;
import com.caco3.mvk.login.LogInModule;
import com.caco3.mvk.splash.SplashComponent;
import com.caco3.mvk.splash.SplashModule;

import static com.caco3.mvk.util.Preconditions.checkState;

public class DaggerComponentsHolder {
  private static final DaggerComponentsHolder INSTANCE = new DaggerComponentsHolder();
  private ApplicationComponent applicationComponent;
  private LogInComponent logInComponent;
  private SplashComponent splashComponent;

  public static DaggerComponentsHolder getInstance() {
    return INSTANCE;
  }

  private DaggerComponentsHolder(){
  }

  public void initApplicationComponent(Context context) {
    checkState(applicationComponent == null, "Application component is already initialized");
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
}
