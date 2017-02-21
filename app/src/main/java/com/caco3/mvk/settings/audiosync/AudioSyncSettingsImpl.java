package com.caco3.mvk.settings.audiosync;

import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

import static com.caco3.mvk.util.Preconditions.checkNotNull;
import static com.caco3.mvk.util.Preconditions.checkState;

public class AudioSyncSettingsImpl implements AudioSyncSettings {
  static final String KEY_PREFERENCES_PREFIX = "audio_sync";
  static final String SYNC_ENABLED_KEY = KEY_PREFERENCES_PREFIX + "_enabled";
  static final String ONLY_VIA_WIFI_KEY = KEY_PREFERENCES_PREFIX + "_only_via_wifi";
  static final String INTERVAL_KEY = KEY_PREFERENCES_PREFIX + "_interval";

  private final SharedPreferences sharedPreferences;

  public AudioSyncSettingsImpl(SharedPreferences sharedPreferences) {
    this.sharedPreferences = checkNotNull(sharedPreferences, "sharedPreferences == null");
  }

  @Override
  public boolean isSyncOnlyViaWifiAllowed() {
    return sharedPreferences.getBoolean(ONLY_VIA_WIFI_KEY, true);
  }

  @Override
  public boolean isSyncEnabled() {
    return sharedPreferences.getBoolean(SYNC_ENABLED_KEY, false);
  }

  @Override
  public long getSyncIntervalMillis() {
    long value = sharedPreferences.getLong(INTERVAL_KEY, TimeUnit.HOURS.toMillis(8));
    checkState(value > 0, String.format("interval value is too small (%d)", value));
    return value;
  }
}
