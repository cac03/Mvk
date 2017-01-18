package com.caco3.mvk.audios;

import android.content.Context;

import com.caco3.mvk.audiodownload.AudioDownloadDirectoryProvider;
import com.caco3.mvk.audiodownload.AudioDownloadPresenter;
import com.caco3.mvk.audiodownload.AudioDownloadPresenterImpl;
import com.caco3.mvk.audiodownload.AudioDownloadView;
import com.caco3.mvk.audiodownload.DownloadViewNotificationsImpl;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.loggedin.LoggedInScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class AudiosModule {
  @Provides
  @LoggedInScope
  public AudioDownloadPresenter
  provideAudioDownloadPresenter(OkHttpClient okHttpClient,
                                AudioDownloadDirectoryProvider directoryProvider,
                                AudiosRepository audiosRepository,
                                @Named("notificationsView") AudioDownloadView audioDownloadView) {
    return new AudioDownloadPresenterImpl(okHttpClient, directoryProvider,
            audiosRepository, audioDownloadView);
  }

  @Provides
  @Named("notificationsView")
  @LoggedInScope
  public AudioDownloadView provideAudioDownloadView(Context context) {
    // FIXME: 1/18/17
    return null;
  }

  @Provides
  @LoggedInScope
  public AudioDownloadDirectoryProvider provideAudioDownloadDirectoryProvider() {
    // FIXME: 1/18/17
    return null;
  }
}
