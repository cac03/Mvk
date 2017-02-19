package com.caco3.mvk.util;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.caco3.mvk.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowPendingIntent;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 23
)
public class AlarmsTest {
  private Context context;
  private ShadowAlarmManager shadowAlarmManager;

  @Before public void setUp() throws Exception {
    context = RuntimeEnvironment.application;
    shadowAlarmManager = shadowOf((AlarmManager)context.getSystemService(Context.ALARM_SERVICE));
  }

  @Test public void alarmCorrectlyScheduled() {
    long interval = TimeUnit.HOURS.toMillis(4);
    long triggerAt = SystemClock.elapsedRealtime() + interval;
    Alarms.scheduleAlarm(context, DummyBroadcastReceiver.class, interval);
    ShadowAlarmManager.ScheduledAlarm scheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();

    assertThat(scheduledAlarm.triggerAtTime)
            .isEqualTo(triggerAt);
    assertThat(scheduledAlarm.interval)
            .isEqualTo(interval);
    ShadowPendingIntent shadowPendingIntent = shadowOf(scheduledAlarm.operation);
    assertThat(shadowPendingIntent.isBroadcastIntent())
            .isTrue();
    assertThat(shadowPendingIntent.getSavedIntent().getComponent().getClassName())
            .isEqualTo(DummyBroadcastReceiver.class.getName());
  }

  @Test public void alarmCorrectlyCanceled() {
    Alarms.scheduleAlarm(context, DummyBroadcastReceiver.class, 1_000_000);
    Alarms.cancelAlarm(context, DummyBroadcastReceiver.class);
    assertThat(shadowAlarmManager.getScheduledAlarms())
            .isEmpty();
  }

  private static class DummyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    }
  }
}
