package com.caco3.mvk.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.caco3.mvk.boot.command.BootCommand;

import java.util.List;

import javax.inject.Inject;

public class BootBroadcastReceiver extends BroadcastReceiver {
  @Inject List<BootCommand> commands;



  @Override public void onReceive(Context context, Intent intent) {
    inject();
    if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
      for(BootCommand command : commands) {
        command.execute();
      }
    }
  }

  private void inject() {
    // TODO: 2/22/17 inject
  }
}
