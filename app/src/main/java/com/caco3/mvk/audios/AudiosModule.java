package com.caco3.mvk.audios;

import android.content.Context;

import com.caco3.mvk.audiodownload.AudioDownloadDirectoryProvider;
import com.caco3.mvk.audiodownload.AudioDownloader;
import com.caco3.mvk.audiodownload.AudioDownloaderImpl;
import com.caco3.mvk.audiodownload.DownloadViewNotificationsImpl;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.loggedin.LoggedInScope;
import com.caco3.mvk.rxbus.RxBus;
import com.caco3.mvk.storage.dir.DirectoryProvider;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class AudiosModule {

  @Provides
  @LoggedInScope
  public AudioDownloadDirectoryProvider provideAudioDownloadDirectoryProvider(DirectoryProvider directoryProvider) {
    return new AudioDownloadDirectoryProvider(directoryProvider);
  }

  @Provides
  @LoggedInScope
  public AudioDownloader provideAudioDownloader(Context context) {
    return new AudioDownloaderImpl(context);
  }
}
