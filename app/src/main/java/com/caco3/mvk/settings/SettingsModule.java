package com.caco3.mvk.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.caco3.mvk.settings.audiosync.AudioSyncPreferencesChangeListener;
import com.caco3.mvk.settings.audiosync.AudioSyncSettings;
import com.caco3.mvk.settings.audiosync.AudioSyncSettingsImpl;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module public class SettingsModule {

  @Provides @Singleton @Named("default")
  public SharedPreferences provideSharedPreferences(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Provides @Singleton
  public AudioSyncSettings provideAudioSyncSettings(@Named("default") SharedPreferences sharedPreferences) {
    return new AudioSyncSettingsImpl(sharedPreferences);
  }

  @Provides @Singleton
  public List<SharedPreferences.OnSharedPreferenceChangeListener>
      provideOnSharedPreferenceChangeListeners(Context context, AudioSyncSettings audioSyncSettings) {
    return Arrays.<SharedPreferences.OnSharedPreferenceChangeListener>
            asList(new AudioSyncPreferencesChangeListener(context, audioSyncSettings));
  }
}
