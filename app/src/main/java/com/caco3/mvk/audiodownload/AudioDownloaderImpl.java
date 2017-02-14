package com.caco3.mvk.audiodownload;

import android.content.Context;

import com.caco3.mvk.vk.audio.Audio;

public class AudioDownloaderImpl implements AudioDownloader {
  private final Context context;

  public AudioDownloaderImpl(Context context) {
    this.context = context;
  }

  @Override
  public void post(Audio audio) {
    context.startService(AudioDownloadService.forAudio(context, audio));
  }
}
