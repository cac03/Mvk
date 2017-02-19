package com.caco3.mvk.audiosync.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.caco3.mvk.audiosync.SyncAudiosService;

import timber.log.Timber;

public class SyncAudiosAlarmReceiver extends WakefulBroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    Timber.d("onReceive()");
    startWakefulService(context, new Intent(context, SyncAudiosService.class));
  }
}
