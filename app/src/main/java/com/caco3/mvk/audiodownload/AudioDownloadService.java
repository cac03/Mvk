package com.caco3.mvk.audiodownload;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.caco3.mvk.audiodownload.events.AudioAcceptedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadProgressUpdatedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadedEvent;
import com.caco3.mvk.audiodownload.events.UnableDownloadAudioEvent;
import com.caco3.mvk.audiodownload.storage.AudioDownloadDirectoryProvider;
import com.caco3.mvk.audiodownload.storage.MvkAudioFile;
import com.caco3.mvk.dagger.DaggerComponentsHolder;
import com.caco3.mvk.rxbus.RxBus;
import com.caco3.mvk.util.io.BytesTransfer;
import com.caco3.mvk.util.io.Closeables;
import com.caco3.mvk.vk.audio.Audio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

import static com.caco3.mvk.util.Preconditions.checkArgument;
import static com.caco3.mvk.util.Preconditions.checkNotNull;
import static com.caco3.mvk.util.Preconditions.checkState;

public class AudioDownloadService extends Service {
  static final int PROGRESS_UPDATED_POSTING_INTERVAL_MILLIS = 250;
  static final String WAKE_LOCK_TAG = "AudioDownloadServiceWakeLockTag";
  static final String EXTRA_AUDIO = "audio";

  Executor executor = Executors.newSingleThreadExecutor();
  final AtomicInteger audiosProcessing = new AtomicInteger();

  volatile PowerManager.WakeLock wakeLock;
  @Inject
  OkHttpClient okHttpClient;
  @Inject
  AudioDownloadDirectoryProvider directoryProvider;
  @Inject
  RxBus rxBus;
  private long lastProgressUpdatedEventPostedTimeMillis;

  public static Intent forAudio(Context context, Audio audio) {
    checkNotNull(audio, "audio == null");
    return new Intent(context, AudioDownloadService.class)
            .putExtra(EXTRA_AUDIO, audio);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    DaggerComponentsHolder.getInstance()
            .getLoggedInComponent().inject(this);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    checkIntent(intent);

    acquireWakeLock();
    Audio audio = (Audio)intent.getSerializableExtra(EXTRA_AUDIO);
    postAudio(audio);

    return super.onStartCommand(intent, flags, startId);
  }

  @SuppressLint("DefaultLocale")
  private void checkIntent(Intent intent) {
    checkState(audiosProcessing.get() >= 0,
            String.format("audioDownloading count is negative (%d)", audiosProcessing.get()));
    Bundle extras = intent.getExtras();
    checkArgument(extras != null, "No extras in intent");
    checkArgument(extras.containsKey(EXTRA_AUDIO),
            String.format("No Audio in the intent (%s)", intent));
  }

  private void acquireWakeLock() {
    if (wakeLock == null) {
      PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
      wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);
    }
    if (!wakeLock.isHeld()) {
      wakeLock.acquire();
    }
  }

  private void postAudio(Audio audio) {
    audiosProcessing.incrementAndGet();
    rxBus.post(new AudioAcceptedEvent(audio));
    Timber.d("Going to download audio: '%s', audiosProcessing = '%d'",
            audio, audiosProcessing.get());

    executor.execute(new DownloadAudioTask(audio));
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }


  private class DownloadAudioTask implements Runnable {
    private final Audio audio;

    DownloadAudioTask(Audio audio) {
      this.audio = audio;
    }

    @Override
    public void run() {
      checkState(wakeLock.isHeld(), "WakeLock is not acquired");
      MvkAudioFile audioFile = null;
      final File downloadInto;
      OutputStream out = null;
      InputStream in = null;
      Response response = null;
      try {

        audioFile = new MvkAudioFile(directoryProvider.getDirectory(), audio);
        downloadInto = audioFile.prepareForDownload();

        out = new FileOutputStream(downloadInto);

        String url = audio.getDownloadUrl();
        response = okHttpClient.newCall(new Request.Builder().url(url).build()).execute();
        in = response.body().byteStream();
        transfer(audio, in, out, response.body().contentLength());
        audioFile.restoreAfterDownload();
        rxBus.post(new AudioDownloadedEvent(audio));
        Timber.d("Audio is '%s' successfully downloaded", audio);
      } catch (IOException e) {
        Timber.e(e, "Unable to download audio ('%s')", audio);
        if (audioFile != null) {
          audioFile.remove();
        }
        rxBus.post(new UnableDownloadAudioEvent(e, audio));
      } finally {
        Closeables.closeOrLog(response);
        Closeables.closeOrLog(out);
        Closeables.closeOrLog(in);
        if (audiosProcessing.decrementAndGet() == 0) {
          wakeLock.release();
        }
        Timber.d("finally: audiosProcessing = '%d'", audiosProcessing.get());
      }
    }
  }

  private void transfer(final Audio audio, InputStream in,
                        OutputStream out, final long contentLength) throws IOException {
    /**
     * {@link BytesTransfer} internally uses {@link InputStream#available()}
     * to determine how many bytes total available, but {@link InputStream}
     * from {@link okhttp3.ResponseBody} returns 0,
     * and we have to use contentLength instead
     */
    new BytesTransfer(out, in)
            .transfer(new BytesTransfer.ProgressListener() {
              @Override
              public void update(long bytesRead, long bytesTotal, long nanosElapsed) {
                updateProgress(audio, bytesRead, contentLength, nanosElapsed);
              }
            });
  }

  private void updateProgress(Audio downloading, long bytesRead,
                              long bytesTotal, long nanosElapsed) {
    if (!needToPostProgressUpdatedEvent()) {
      return;
    }
    lastProgressUpdatedEventPostedTimeMillis = System.currentTimeMillis();
    rxBus.post(AudioDownloadProgressUpdatedEvent
            .builder()
            .audio(downloading)
            .bytesDownloaded(bytesRead)
            .bytesTotal(bytesTotal)
            .nanosElapsed(nanosElapsed)
            .build());
  }

  private boolean needToPostProgressUpdatedEvent(){
    long now = System.currentTimeMillis();
    return now - lastProgressUpdatedEventPostedTimeMillis
            >= PROGRESS_UPDATED_POSTING_INTERVAL_MILLIS;
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      if (wakeLock != null && wakeLock.isHeld()) {
        Timber.wtf("WakeLock is not released!");
        wakeLock.release();
      }
    } finally {
      super.finalize();
    }
  }
}
