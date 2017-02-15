package com.caco3.mvk.audiodownload;

import android.content.Context;

import com.caco3.mvk.vk.audio.Audio;

import rx.android.schedulers.AndroidSchedulers;

public class AudioDownloaderImpl implements AudioDownloader {
  private final Context context;

  public AudioDownloaderImpl(Context context,
                             AudioDownloadNotificationsSender notificationsSender) {
    this.context = context;
    // TODO: 2/15/17 When to stop handling?
    notificationsSender.startHandling(AndroidSchedulers.mainThread());
  }

  @Override
  public void post(Audio audio) {
    context.startService(AudioDownloadService.forAudio(context, audio));
  }
}
