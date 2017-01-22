package com.caco3.mvk.splash;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.caco3.mvk.dagger.DaggerComponentsHolder;
import com.caco3.mvk.ui.BaseActivity;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {
  @Inject
  LauncherActivityManager launcherActivityManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectFields();
    startActivity(new Intent(this, launcherActivityManager.getLauncherActivity()));
    finish();
    releaseSplashComponent();
  }

  private void injectFields() {
    DaggerComponentsHolder componentsHolder = DaggerComponentsHolder.getInstance();
    componentsHolder.getSplashComponent().inject(this);
  }

  private void releaseSplashComponent() {
    DaggerComponentsHolder.getInstance().releaseSplashComponent();
  }
}
