package com.caco3.mvk.settings.audiosync;

import android.content.Context;
import android.content.SharedPreferences;

import com.caco3.mvk.audiosync.receiver.SyncAudiosAlarmReceiver;
import com.caco3.mvk.util.Alarms;

import static com.caco3.mvk.util.Preconditions.checkArgument;
import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class AudioSyncPreferencesChangeListener
        implements SharedPreferences.OnSharedPreferenceChangeListener {
  private AudioSyncSettings settings;
  private Context context;

  public AudioSyncPreferencesChangeListener(Context context, AudioSyncSettings settings) {
    this.settings = checkNotNull(settings, "settings == null");
    this.context = checkNotNull(context, "context == null");
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.startsWith(AudioSyncSettingsImpl.KEY_PREFERENCES_PREFIX)) {
      if (key.equals(AudioSyncSettingsImpl.INTERVAL_KEY)) {
        rescheduleSyncService(settings.getSyncIntervalMillis());
      } else if (key.equals(AudioSyncSettingsImpl.ONLY_VIA_WIFI_KEY)) {
        // TODO: 2/21/17 ignore and use settings methods in the service?
      } else if (key.equals(AudioSyncSettingsImpl.SYNC_ENABLED_KEY)) {
        onSyncEnabledSettingChanged();
      }
    }
  }

  private void rescheduleSyncService(long newInterval) {
    checkArgument(newInterval > 0, String.format("newInterval is too small(%d)", newInterval));
    Alarms.cancelAlarm(context, SyncAudiosAlarmReceiver.class);
    Alarms.scheduleAlarm(context, SyncAudiosAlarmReceiver.class, newInterval);
  }

  private void onSyncEnabledSettingChanged() {
    if (settings.isSyncEnabled()) {
      Alarms.scheduleAlarm(context, SyncAudiosAlarmReceiver.class, settings.getSyncIntervalMillis());
    } else {
      Alarms.cancelAlarm(context, SyncAudiosAlarmReceiver.class);
    }
  }
}
