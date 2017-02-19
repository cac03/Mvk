package com.caco3.mvk.audiosync;

import android.app.IntentService;
import android.content.Intent;

import com.caco3.mvk.audiodownload.AudioDownloader;
import com.caco3.mvk.audiosync.receiver.SyncAudiosAlarmReceiver;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.VkException;
import com.caco3.mvk.vk.audio.Audio;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import timber.log.Timber;

public class SyncAudiosService extends IntentService {
  @Inject
  Vk vk;
  @Inject
  AppUser appUser;
  @Inject
  AudiosRepository audiosRepository;
  @Inject
  AudioDownloader audioDownloader;
  @Inject
  List<AudioSyncPolicy> syncPolicies;
  private AudiosToDownloadExtractor audiosToDownloadExtractor = new AudiosToDownloadExtractor();

  public SyncAudiosService() {
    super("SyncAudiosService"); // worker thread name
  }

  @Override
  protected void onHandleIntent(final Intent intent) {
    Timber.d("Running");
    if (!canSync()) {
      releaseWakeLock(intent);
      return;
    }
    fetchNewAudios().subscribe(new Subscriber<List<Audio>>() {
      @Override
      public void onCompleted() {
        releaseWakeLock(intent);
      }

      @Override
      public void onError(Throwable e) {
        releaseWakeLock(intent);
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
        for(Audio audio : audiosToDownloadExtractor.extract(audios)) {
          audioDownloader.post(audio);
        }
      }
    });
  }

  private void releaseWakeLock(Intent intent) {
    SyncAudiosAlarmReceiver.completeWakefulIntent(intent);
  }

  private boolean canSync() {
    for(AudioSyncPolicy syncPolicy : syncPolicies) {
      if (!syncPolicy.canSync()) {
        Timber.d("%s has not allowed to sync", syncPolicy.getClass().getSimpleName());
        return false;
      }
    }

    return true;
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
}
