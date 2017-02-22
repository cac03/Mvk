package com.caco3.mvk.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.caco3.mvk.boot.command.BootCommand;
import com.caco3.mvk.dagger.DaggerComponentsHolder;

import java.util.List;

import javax.inject.Inject;

public class BootBroadcastReceiver extends BroadcastReceiver {
  @Inject List<BootCommand> commands;

  public BootBroadcastReceiver() {
    inject();
  }

  @Override public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
      for(BootCommand command : commands) {
        command.execute();
      }
    }
  }

  private void inject() {
    DaggerComponentsHolder.getInstance()
            .getApplicationComponent().inject(this);
  }
}
