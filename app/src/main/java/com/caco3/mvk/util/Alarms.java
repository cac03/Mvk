package com.caco3.mvk.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Date;

import timber.log.Timber;

import static com.caco3.mvk.util.Preconditions.checkArgument;
import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class Alarms {
  private Alarms() {
    throw new AssertionError("No " + Alarms.class.getSimpleName() + " instances");
  }

  public static void scheduleAlarm(Context context, Class<? extends BroadcastReceiver> receiverClass,
                                   long intervalMillis) {
    checkNotNull(context, "context == null");
    checkNotNull(receiverClass, "receiverClass");
    checkArgument(intervalMillis > 0, "intervalMillis is too small (" + intervalMillis + ")");
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(context, receiverClass);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

    long nextTriggerAt = SystemClock.elapsedRealtime() + intervalMillis;
    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
            nextTriggerAt,
            intervalMillis,
            pendingIntent);
    Timber.d("Alarm scheduled with %ds interval. Next time it will trigger at: ", intervalMillis / 1000,
            new Date(nextTriggerAt).toString());
  }

  public static void cancelAlarm(Context context, Class<? extends BroadcastReceiver> receiverClass) {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(context, receiverClass);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    alarmManager.cancel(pendingIntent);
    Timber.d("Alarm canceled. Receiver: %s", receiverClass.getSimpleName());
  }
}
