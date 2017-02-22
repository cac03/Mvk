package com.caco3.mvk.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.XmlRes;

import com.caco3.mvk.R;
import com.caco3.mvk.dagger.DaggerComponentsHolder;

import java.util.List;

import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
  @XmlRes private static final int PREFERENCES_RES = R.xml.preferences;
  @Inject List<SharedPreferences.OnSharedPreferenceChangeListener> listeners;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(PREFERENCES_RES);
    DaggerComponentsHolder.getInstance().getApplicationComponent()
            .inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    getPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  private SharedPreferences getPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(getActivity());
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    for(SharedPreferences.OnSharedPreferenceChangeListener listener : listeners) {
      listener.onSharedPreferenceChanged(sharedPreferences, key);
    }
  }

  @Override
  public void onPause() {
    getPreferences().unregisterOnSharedPreferenceChangeListener(this);
    super.onPause();
  }
}
