package com.caco3.mvk.audiodownload.events;

import com.caco3.mvk.vk.audio.Audio;

import static com.caco3.mvk.util.Preconditions.checkState;

public class AudioDownloadProgressUpdatedEvent {
  private final Audio audio;
  private final long nanosElapsed;
  private final long bytesTotal;
  private final long bytesDownloaded;

  private AudioDownloadProgressUpdatedEvent(Builder builder) {
    this.audio = builder.audio;
    this.nanosElapsed = builder.nanosElapsed;
    this.bytesDownloaded = builder.bytesDownloaded;
    this.bytesTotal = builder.bytesTotal;
  }

  public Audio getAudio() {
    return audio;
  }

  public long getNanosElapsed() {
    return nanosElapsed;
  }

  public long getBytesTotal() {
    return bytesTotal;
  }

  public long getBytesDownloaded() {
    return bytesDownloaded;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Audio audio;
    private long nanosElapsed;
    private long bytesTotal;
    private long bytesDownloaded;

    public Builder audio(Audio audio) {
      this.audio = audio;
      return this;
    }

    public Builder nanosElapsed(long nanosElapsed) {
      this.nanosElapsed = nanosElapsed;
      return this;
    }

    public Builder bytesTotal(long bytesTotal) {
      this.bytesTotal = bytesTotal;
      return this;
    }

    public Builder bytesDownloaded(long bytesDownloaded) {
      this.bytesDownloaded = bytesDownloaded;
      return this;
    }

    public AudioDownloadProgressUpdatedEvent build() {
      checkState(audio != null, "audio must be set");
      return new AudioDownloadProgressUpdatedEvent(this);
    }
  }
}
