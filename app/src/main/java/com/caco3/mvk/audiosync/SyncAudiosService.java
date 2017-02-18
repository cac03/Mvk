package com.caco3.mvk.audiosync;

import android.app.IntentService;
import android.content.Intent;

import com.caco3.mvk.audiodownload.AudioDownloader;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.network.NetworkManager;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.VkException;
import com.caco3.mvk.vk.audio.Audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import timber.log.Timber;

// TODO: 2/18/17 Better name?
public class SyncAudiosService extends IntentService {
  @Inject
  Vk vk;
  @Inject
  AppUser appUser;
  @Inject
  AudiosRepository audiosRepository;
  @Inject
  AudioDownloader audioDownloader;
  // TODO: 2/18/17 Replace this with SomeAudioSyncNetworkPolicy
  @Inject
  NetworkManager networkManager;

  public SyncAudiosService() {
    super("SyncAudiosService"); // worker thread name
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Timber.d("Running");
    // TODO: 2/18/17 someAudioSyncNetworkPolicy.canSync();
    if (!networkManager.isConnectedWithWiFi()) {
      Timber.d("Connected not via WiFi. Exiting");
      return;
    }
    fetchNewAudios().subscribe(new Subscriber<List<Audio>>() {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        Timber.e(e, "Unable to fetch new audios");
        if (isRethrowNeeded(e)) {
          throw Exceptions.propagate(e);
        }
      }

      @Override
      public void onNext(List<Audio> audios) {
        Timber.d("Successfully fetched new audios. Size: %d", audios.size());
        for(int i = 0, length = audios.size(); i < length; i++) {
          audios.get(i).setVkPlaylistPosition(i);
        }
        audiosRepository.replaceAllByVkUserId(appUser.getUserToken().getVkUserId(), audios);
        for(Audio audio : extractAudiosToDownload(audios)) {
          audioDownloader.post(audio);
        }
      }
    });
  }

  private Observable<List<Audio>> fetchNewAudios() {
    return Observable.fromCallable(new Callable<List<Audio>>() {
      @Override
      public List<Audio> call() throws Exception {
        return vk.audios().get();
      }
    });
  }

  private boolean isRethrowNeeded(Throwable t) {
    return !(t instanceof IOException || t instanceof VkException);
  }

  // TODO: 2/18/17 Move it to another class?
  /*package*/ List<Audio> extractAudiosToDownload(List<Audio> audios) {
    List<Audio> toDownload = new ArrayList<>();
    int length = audios.size() >= 20 ? 20 : audios.size();
    for(int i = 0; i < length; i++) {
      Audio audio = audios.get(i);
      if (!audio.isDownloaded() && audio.isAvailableForDownload()) {
        toDownload.add(audio);
      }
    }
    return toDownload;
  }
}
