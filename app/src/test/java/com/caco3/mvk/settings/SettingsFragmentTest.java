package com.caco3.mvk.settings;

import android.preference.PreferenceFragment;

import com.caco3.mvk.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.robolectric.util.FragmentTestUtil.startFragment;


@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 22
)
public class SettingsFragmentTest {
  private PreferenceFragment fragment;

  @Before public void setUp() throws Exception {
    fragment = new SettingsFragment();
    startFragment(fragment);
  }

  @Test public void fragmentStarted_settingsDisplayed() {
    assertThat(fragment.getPreferenceScreen())
            .isNotNull();
  }
}
