package com.caco3.mvk.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.XmlRes;

import com.caco3.mvk.R;

public class SettingsFragment extends PreferenceFragment {
  @XmlRes private static final int PREFERENCES_RES = R.xml.preferences;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(PREFERENCES_RES);
  }
}
