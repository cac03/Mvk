package com.caco3.mvk.boot.command;

import android.content.Context;

import com.caco3.mvk.audiosync.receiver.SyncAudiosAlarmReceiver;
import com.caco3.mvk.settings.audiosync.AudioSyncSettings;
import com.caco3.mvk.util.Alarms;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class RescheduleAlarmCommand implements BootCommand {
  private final Context context;
  private final AudioSyncSettings settings;

  public RescheduleAlarmCommand(Context context, AudioSyncSettings settings) {
    this.context = checkNotNull(context, "context == null");
    this.settings = checkNotNull(settings, "settings == null");
  }

  @Override
  public void execute() {
    if (settings.isSyncEnabled()) {
      long interval = settings.getSyncIntervalMillis();
      Alarms.scheduleAlarm(context, SyncAudiosAlarmReceiver.class, interval);
    }
  }
}
