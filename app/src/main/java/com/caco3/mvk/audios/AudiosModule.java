package com.caco3.mvk.audios;

import android.content.Context;

import com.caco3.mvk.audiodownload.AudioDownloadNotificationsSender;
import com.caco3.mvk.audiodownload.storage.AudioDownloadDirectoryProvider;
import com.caco3.mvk.audiodownload.AudioDownloader;
import com.caco3.mvk.audiodownload.AudioDownloaderImpl;
import com.caco3.mvk.loggedin.LoggedInScope;
import com.caco3.mvk.rxbus.RxBus;
import com.caco3.mvk.storage.dir.DirectoryProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class AudiosModule {

  @Provides
  @LoggedInScope
  public AudioDownloadDirectoryProvider provideAudioDownloadDirectoryProvider(DirectoryProvider directoryProvider) {
    return new AudioDownloadDirectoryProvider(directoryProvider);
  }

  @Provides
  @LoggedInScope
  public AudioDownloader provideAudioDownloader(Context context,
                                                AudioDownloadNotificationsSender notificationsSender) {
    return new AudioDownloaderImpl(context, notificationsSender);
  }

  @Provides
  @LoggedInScope
  public AudioDownloadNotificationsSender provideAudioDownloadNotificationsSender(Context context,
                                                                                  RxBus rxBus) {
    return new AudioDownloadNotificationsSender(context, rxBus);
  }
}
