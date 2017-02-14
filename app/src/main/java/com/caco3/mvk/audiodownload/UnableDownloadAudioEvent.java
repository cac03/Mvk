package com.caco3.mvk.audiodownload;

import com.caco3.mvk.vk.audio.Audio;

public class UnableDownloadAudioEvent {
  private final Throwable cause;
  private final Audio audio;

  public UnableDownloadAudioEvent(Throwable cause, Audio audio) {
    this.cause = cause;
    this.audio = audio;
  }

  public Throwable getCause() {
    return cause;
  }

  public Audio getAudio() {
    return audio;
  }
}
