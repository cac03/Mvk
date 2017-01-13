package com.caco3.mvk;

import android.app.Application;

import com.caco3.mvk.dagger.DaggerComponentsHolder;

import timber.log.Timber;

public class MvkApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    initTimber();
    initDaggerComponents();
  }

  private void initTimber() {
    Timber.plant(new Timber.DebugTree());
  }

  private void initDaggerComponents() {
    DaggerComponentsHolder
            .getInstance()
            .initApplicationComponent(this);
  }
}
