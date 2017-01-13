package com.caco3.mvk.dagger;

import android.content.Context;

import com.caco3.mvk.ApplicationComponent;
import com.caco3.mvk.ApplicationModule;
import com.caco3.mvk.DaggerApplicationComponent;

import static com.caco3.mvk.util.Preconditions.checkState;

public class DaggerComponentsHolder {
  private static final DaggerComponentsHolder INSTANCE = new DaggerComponentsHolder();
  private ApplicationComponent applicationComponent;

  public static DaggerComponentsHolder getInstance() {
    return INSTANCE;
  }

  private DaggerComponentsHolder(){
  }

  public void initApplicationComponent(Context context) {
    checkState(applicationComponent == null, "Application component is already initialized");
    applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(context))
            .build();
  }
}
