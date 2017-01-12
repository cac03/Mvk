package com.caco3.mvk;

import android.app.Application;

import timber.log.Timber;

public class MvkApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    initTimber();
  }

  private void initTimber() {
    Timber.plant(new Timber.DebugTree());
  }
}
