package com.caco3.mvk.audiodownload;

import com.caco3.mvk.mvp.BasePresenter;
import com.caco3.mvk.vk.audio.Audio;


public interface AudioDownloadPresenter extends BasePresenter<AudioDownloadView> {
  void startDownload(Audio audio);
  void cancelDownload(Audio audio);
}
