package com.caco3.mvk.audiodownload;


import com.caco3.mvk.vk.audio.Audio;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AudioDownloadProgress {
  /*package*/ List<Audio> pending
          = new CopyOnWriteArrayList<>();
  /*package*/ Map<Audio, DownloadProgress> currentlyDownloading
          = new ConcurrentHashMap<>();

  public List<Audio> getPending() {
    // better to return defensive copy, but it might be too expensive
    return pending;
  }

  public Map<Audio, DownloadProgress> getCurrentlyDownloading() {
    // better to return defensive copy, but it might be too expensive
    return currentlyDownloading;
  }

  public static class DownloadProgress {
    private long bytesTotal;
    private long bytesTransferred;
    private long nanosElapsed;

    public long getBytesTotal() {
      return bytesTotal;
    }

    public void setBytesTotal(long bytesTotal) {
      this.bytesTotal = bytesTotal;
    }

    public long getBytesTransferred() {
      return bytesTransferred;
    }

    public void setBytesTransferred(long bytesTransferred) {
      this.bytesTransferred = bytesTransferred;
    }

    public long getNanosElapsed() {
      return nanosElapsed;
    }

    public void setNanosElapsed(long nanosElapsed) {
      this.nanosElapsed = nanosElapsed;
    }
  }
}
