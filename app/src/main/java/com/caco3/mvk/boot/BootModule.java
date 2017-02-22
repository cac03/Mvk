package com.caco3.mvk.boot;

import android.content.Context;

import com.caco3.mvk.boot.command.BootCommand;
import com.caco3.mvk.boot.command.RescheduleAlarmCommand;
import com.caco3.mvk.settings.audiosync.AudioSyncSettings;

import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BootModule {
  @Provides @Singleton
  public List<BootCommand> provideBootCommands(Context context, AudioSyncSettings settings) {
    return Arrays.<BootCommand>asList(
            new RescheduleAlarmCommand(context, settings)
    );
  }
}
