package com.caco3.mvk.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.caco3.mvk.R;
import com.caco3.mvk.ui.UserLoggedInBaseActivity;

public class SettingsActivity extends UserLoggedInBaseActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base);
    getFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, new SettingsFragment())
            .commit();
  }

  @Override protected int getNavDrawerItemId() {
    return R.id.nav_settings;
  }
}
