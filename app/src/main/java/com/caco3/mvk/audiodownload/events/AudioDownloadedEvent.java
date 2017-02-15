package com.caco3.mvk.audiodownload.events;


import com.caco3.mvk.vk.audio.Audio;

public class AudioDownloadedEvent {
  private final Audio audio;

  public AudioDownloadedEvent(Audio audio) {
    this.audio = audio;
  }

  public Audio getAudio() {
    return audio;
  }
}
