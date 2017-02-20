package com.caco3.mvk.settings.audiosync;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.caco3.mvk.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 22
)
public class AudioSyncSettingsImplTest {
  private SharedPreferences preferences;
  private AudioSyncSettings syncSettings;

  @Before public void setUp() throws Exception {
    Context context = RuntimeEnvironment.application;
    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    syncSettings = new AudioSyncSettingsImpl(preferences);
  }

  @After public void tearDown() throws Exception {
    preferences.edit().clear().apply();
  }

  @Test public void constructorInvokedWithNull_npeThrown() {
    try {
      new AudioSyncSettingsImpl(null);
      fail("Npe was not thrown");
    } catch (NullPointerException expected) {
      assertThat(expected.getMessage())
              .isEqualTo("sharedPreferences == null");
    }
  }

  @Test public void isSyncEnabledCalled_falseReturned() {
    assertThat(syncSettings.isSyncEnabled())
            .isEqualTo(false);
  }

  @Test public void isSyncOnlyViaWifiAllowedCalled_trueReturned() {
    assertThat(syncSettings.isSyncOnlyViaWifiAllowed())
            .isTrue();
  }

  @Test public void getSyncIntervalMillisCalled_8hoursInMillisReturned() {
    assertThat(syncSettings.getSyncIntervalMillis())
            .isEqualTo(TimeUnit.HOURS.toMillis(8));
  }

  @Test public void syncEnabledValueSetToTrue_isSyncEnabledReturnsTrue() {
    preferences.edit().putBoolean("audio_sync_enabled", true).apply();
    assertThat(syncSettings.isSyncEnabled())
            .isTrue();
  }

  @Test public void syncOnlyViaWifiSetToFalse_isSyncOnlyViaWifiAllowedReturnsFalse() {
    preferences.edit().putBoolean("audio_sync_only_via_wifi", false).apply();
    assertThat(syncSettings.isSyncOnlyViaWifiAllowed())
            .isFalse();
  }

  @Test public void intervalValueChangedToIllegalValue_getSyncIntervalMillisThrowsIse() {
    preferences.edit().putLong("audio_sync_interval", 0).apply();
    try {
      new AudioSyncSettingsImpl(preferences).getSyncIntervalMillis();
      fail("IllegalStateException was not thrown");
    } catch (IllegalStateException expected) {
      assertThat(expected.getMessage())
              .isEqualTo("interval value is too small (0)");
    }
  }

  @Test public void intervalValueChangedTo4Hours_getSyncIntervalMillisReturns4HoursInMillis() {
    long expected = TimeUnit.HOURS.toMillis(4);
    preferences.edit().putLong("audio_sync_interval", expected).apply();
    assertThat(syncSettings.getSyncIntervalMillis())
            .isEqualTo(expected);
  }
}
