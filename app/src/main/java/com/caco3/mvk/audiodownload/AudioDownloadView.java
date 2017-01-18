package com.caco3.mvk.audiodownload;

import com.caco3.mvk.mvp.BaseView;
import com.caco3.mvk.vk.audio.Audio;


public interface AudioDownloadView extends BaseView<AudioDownloadPresenter> {
  void showDownloadPending(Audio audio);
  void updateProgress(Audio audio, long bytesDownloaded, long bytesTotal, long nanosElapsed);
  void showDownloadFailed(Audio audio);
  void showDownloadSuccessful(Audio audio);
  void showDownloadCanceled(Audio audio);
}
