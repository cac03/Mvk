package com.caco3.mvk.audios;

import android.content.Context;

import com.caco3.mvk.audiodownload.storage.AudioDownloadDirectoryProvider;
import com.caco3.mvk.audiodownload.AudioDownloader;
import com.caco3.mvk.audiodownload.AudioDownloaderImpl;
import com.caco3.mvk.loggedin.LoggedInScope;
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
  public AudioDownloader provideAudioDownloader(Context context) {
    return new AudioDownloaderImpl(context);
  }
}
