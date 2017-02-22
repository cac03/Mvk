package com.caco3.mvk.boot.command;

import android.app.AlarmManager;
import android.content.Context;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.audiosync.receiver.SyncAudiosAlarmReceiver;
import com.caco3.mvk.settings.audiosync.AudioSyncSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowPendingIntent;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 23
)
public class RescheduleAlarmCommandTest {
  @Mock private AudioSyncSettings audioSyncSettings;
  private Context context;
  private ShadowAlarmManager shadowAlarmManager;

  @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    when(audioSyncSettings.getSyncIntervalMillis()).thenReturn(80000L);
    context = RuntimeEnvironment.application;
    shadowAlarmManager = shadowOf((AlarmManager)context.getSystemService(Context.ALARM_SERVICE));
  }

  @Test public void constructorInvokedWithNullContext_npeThrown() {
    try {
      new RescheduleAlarmCommand(null, audioSyncSettings);
      fail("npe was not thrown");
    } catch (NullPointerException expected) {
      assertThat(expected.getMessage())
              .isEqualTo("context == null");
    }
  }

  @Test public void constructorInvokedWithNullSettings_npeThrown() {
    try {
      new RescheduleAlarmCommand(context, null);
      fail("npe was not thrown");
    } catch (NullPointerException expected) {
      assertThat(expected)
              .hasMessage("settings == null");
    }
  }

  @Test public void audioSyncEnabled_alarmRescheduled() {
    when(audioSyncSettings.isSyncEnabled()).thenReturn(true);
    new RescheduleAlarmCommand(context, audioSyncSettings).execute();
    ShadowAlarmManager.ScheduledAlarm alarm = shadowAlarmManager.getNextScheduledAlarm();
    assertThat(alarm.interval)
            .isEqualTo(audioSyncSettings.getSyncIntervalMillis());
    ShadowPendingIntent shadowPendingIntent = shadowOf(alarm.operation);
    assertThat(shadowPendingIntent.getSavedIntent().getComponent().getClassName())
            .isEqualTo(SyncAudiosAlarmReceiver.class.getName());
  }

  @Test public void audioSyncDisabled_alarmWasNotRescheduled() {
    when(audioSyncSettings.isSyncEnabled()).thenReturn(false);
    new RescheduleAlarmCommand(context, audioSyncSettings).execute();
    ShadowAlarmManager.ScheduledAlarm alarm = shadowAlarmManager.getNextScheduledAlarm();
    assertThat(alarm)
            .isNull();
  }
}
