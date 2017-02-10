package com.caco3.mvk.audiodownload;


import com.caco3.mvk.network.FileDownloader;
import com.caco3.mvk.vk.audio.Audio;

import java.util.List;
import java.util.Map;

import rx.Observable;


public interface AudioDownloader {
  void post(Audio audio);
  Observable<Progress> getObservable();

  class Progress {
    /*package*/ Map<Audio, FileDownloader.DownloadProgress> currentlyDownloading;
    /*package*/ List<Audio> pending;

    public Map<Audio, FileDownloader.DownloadProgress> getCurrentlyDownloading() {
      return currentlyDownloading;
    }

    public List<Audio> getPending() {
      return pending;
    }
  }
}
