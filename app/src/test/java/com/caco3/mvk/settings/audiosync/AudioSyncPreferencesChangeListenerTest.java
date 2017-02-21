package com.caco3.mvk.settings.audiosync;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.audiosync.receiver.SyncAudiosAlarmReceiver;
import com.caco3.mvk.timber.SystemOutTree;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowPendingIntent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 22
)
public class AudioSyncPreferencesChangeListenerTest {
  private static final long ALARM_SCHEDULE_TOLERANCE_MILLIS = 500;
  private Context context;
  private AudioSyncSettings audioSyncSettings;
  private SharedPreferences sharedPreferences;
  private AudioSyncPreferencesChangeListener listener;
  private final Timber.Tree systemOutTree = new SystemOutTree();
  private ShadowAlarmManager shadowAlarmManager;

  @Before public void setUp() throws Exception {
    context = RuntimeEnvironment.application;
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    audioSyncSettings = new AudioSyncSettingsImpl(sharedPreferences);
    listener = new AudioSyncPreferencesChangeListener(context, audioSyncSettings);
    sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    shadowAlarmManager = shadowOf((AlarmManager)context.getSystemService(Context.ALARM_SERVICE));
    Timber.plant(systemOutTree);
  }

  @After public void tearDown() throws Exception {
    sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    sharedPreferences.edit().clear();
    Timber.uproot(systemOutTree);
  }

  @Test public void constructorInvokedWithNullContext_npeThrown() {
    try {
      new AudioSyncPreferencesChangeListener(null, audioSyncSettings);
      fail("npe was not thrown");
    } catch (NullPointerException expected) {
      assertThat(expected.getMessage())
              .isEqualTo("context == null");
    }
  }

  @Test public void constructorInvokedWithNullAudioSyncSettings_npeThrown() {
    try {
      new AudioSyncPreferencesChangeListener(context, null);
      fail("npe was not thrown");
    } catch (NullPointerException expected) {
      assertThat(expected.getMessage())
              .isEqualTo("settings == null");
    }
  }

  @Test public void syncIntervalChanged_alarmRescheduled() {
    final long interval = TimeUnit.HOURS.toMillis(12);
    sharedPreferences.edit().putLong("audio_sync_interval", TimeUnit.HOURS.toMillis(12)).apply();
    ShadowAlarmManager.ScheduledAlarm scheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
    ShadowPendingIntent pendingIntent = shadowOf(scheduledAlarm.operation);
    assertThat(pendingIntent)
            .isNotNull();
    assertThat(pendingIntent.getSavedIntent().getComponent().getClassName())
            .isEqualTo(SyncAudiosAlarmReceiver.class.getName());
    assertThat(scheduledAlarm.interval)
            .isEqualTo(interval);
    long triggerAt = SystemClock.elapsedRealtime() + interval;
    assertThat(scheduledAlarm.triggerAtTime)
            .isBetween(triggerAt - ALARM_SCHEDULE_TOLERANCE_MILLIS,
                    triggerAt + ALARM_SCHEDULE_TOLERANCE_MILLIS);
  }

  @Test public void syncDisabled_noAlarmScheduled() {
    sharedPreferences.edit().putBoolean("audio_sync_enabled", false).apply();
    assertThat(shadowAlarmManager.getScheduledAlarms())
            .isEmpty();
  }

  @Test public void syncEnabledAndThenDisabled_noAlarmScheduled() {
    sharedPreferences.edit().putBoolean("audio_sync_enabled", true).apply();
    sharedPreferences.edit().putBoolean("audio_sync_enabled", false).apply();
    assertThat(shadowAlarmManager.getScheduledAlarms())
            .isEmpty();
  }

  @Test public void syncEnabled_alarmScheduled() {
    sharedPreferences.edit().putBoolean("audio_sync_enabled", true).apply();
    List<ShadowAlarmManager.ScheduledAlarm> alarms = shadowAlarmManager.getScheduledAlarms();
    assertThat(alarms)
            .hasSize(1);
    ShadowAlarmManager.ScheduledAlarm alarm = alarms.get(0);
    assertThat(shadowOf(alarm.operation).getSavedIntent().getComponent().getClassName())
            .isEqualTo(SyncAudiosAlarmReceiver.class.getName());
  }
}
