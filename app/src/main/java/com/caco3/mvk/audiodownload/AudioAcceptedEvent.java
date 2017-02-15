package com.caco3.mvk.audiodownload;

import com.caco3.mvk.vk.audio.Audio;

public class AudioAcceptedEvent {
  private final Audio audio;

  public AudioAcceptedEvent(Audio audio) {
    this.audio = audio;
  }

  public Audio getAudio() {
    return audio;
  }
}
