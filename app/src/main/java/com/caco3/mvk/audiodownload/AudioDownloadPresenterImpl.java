package com.caco3.mvk.audiodownload;


import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.network.FileDownloader;
import com.caco3.mvk.vk.audio.Audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.caco3.mvk.util.Preconditions.checkArgument;
import static com.caco3.mvk.util.Preconditions.checkNotNull;


public class AudioDownloadPresenterImpl implements AudioDownloadPresenter {
  private static final int THROTTLE_UPDATES_INTERVAL = 200;

  private final AudioDownloadViewsComposite views = new AudioDownloadViewsComposite();

  private final AudioDownloadDirectoryProvider directoryProvider;
  private final OkHttpClient okHttpClient;
  private final AudiosRepository audiosRepository;

  private final Map<Audio, MvkAudioFile> audioFiles = new HashMap<>();
  private final Map<Audio, Subscription> downloadingAudios = new HashMap<>();

  public AudioDownloadPresenterImpl(OkHttpClient okHttpClient,
                                    AudioDownloadDirectoryProvider directoryProvider,
                                    AudiosRepository audiosRepository,
                                    AudioDownloadView notificationsView) {
    this.okHttpClient = okHttpClient;
    this.directoryProvider = directoryProvider;
    this.audiosRepository = audiosRepository;
    onViewAttached(notificationsView);
  }

  @Override
  public void onViewAttached(AudioDownloadView view) {
    checkNotNull(view, "view == null");
    views.add(view);
    initView(view);
  }

  @Override
  public void onViewDetached(AudioDownloadView view) {
    views.remove(view);
  }

  private void initView(AudioDownloadView view) {
    for(Audio audio : audioFiles.keySet()) {
      view.showDownloadPending(audio);
    }
  }

  @Override
  public void startDownload(Audio audio) {
    checkArgument(!downloadingAudios.containsKey(audio), audio + " is already downloading");
    views.showDownloadPending(audio);
    try {
      File file = prepareFile(audio);
      Subscription subscription = download(audio, file);
      downloadingAudios.put(audio, subscription);
    } catch (IOException e) {
      Timber.e(e, "Unable to prepare file for downloading.");
      views.showDownloadFailed(audio);
    }
  }

  private File prepareFile(Audio audio) throws IOException {
    MvkAudioFile audioFile = new MvkAudioFile(directoryProvider.getDirectory(), audio);
    audioFiles.put(audio, audioFile);

    return audioFile.prepareForDownload();
  }

  private Subscription download(Audio audio, File file) {
    return new FileDownloader(okHttpClient, HttpUrl.parse(audio.getDownloadUrl()), file)
            .download()
            .throttleLast(THROTTLE_UPDATES_INTERVAL, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new AudioDownloadSubscriber(audio));
  }

  @Override
  public void cancelDownload(Audio audio) {
    Timber.d("Going to cancel downloading audio '%s'", audio);
    downloadingAudios.get(audio).unsubscribe();
    downloadingAudios.remove(audio);
    audioFiles.get(audio).remove();
    audioFiles.remove(audio);
    views.showDownloadCanceled(audio);
  }

  private class AudioDownloadSubscriber extends Subscriber<FileDownloader.DownloadProgress> {
    private Audio audio;

    AudioDownloadSubscriber(Audio audio) {
      this.audio = audio;
    }

    @Override
    public void onCompleted() {
      views.showDownloadSuccessful(audio);
      audioFiles.get(audio).restoreAfterDownload();
      cleanUpAfterSuccessfulDownload(audio);
      audio.setDownloaded(true);
      audiosRepository.update(audio);
    }

    @Override
    public void onError(Throwable e) {
      Timber.e(e, "Error occurred while downloading audio '%s'. File: '%s'",
              audio, audioFiles.get(audio));
      views.showDownloadFailed(audio);
      cleanUpAfterFailedDownload(audio);
      if (e instanceof IOException) {
        Timber.e("No network?");
      } else {
        throw Exceptions.propagate(e);
      }
    }

    @Override
    public void onNext(FileDownloader.DownloadProgress downloadProgress) {
      views.updateProgress(audio, downloadProgress.getBytesDownloaded(),
              downloadProgress.getTotalBytes(), downloadProgress.getNanosElapsed());
    }
  }

  private void cleanUpAfterSuccessfulDownload(Audio audio) {
    downloadingAudios.remove(audio);
    audioFiles.remove(audio);
  }

  private void cleanUpAfterFailedDownload(Audio audio) {
    audioFiles.get(audio).remove();
    downloadingAudios.remove(audio);
    audioFiles.remove(audio);
  }
}
