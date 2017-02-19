package com.caco3.mvk.audiosync;

import com.caco3.mvk.vk.audio.Audio;

import java.util.ArrayList;
import java.util.List;

public class AudiosToDownloadExtractor {
  static final int MAX_NUMBER_OF_AUDIOS_TO_DOWNLOAD = 20;


  public List<Audio> extract(List<Audio> allAudios) {
    int length;
    if (allAudios.size() >= MAX_NUMBER_OF_AUDIOS_TO_DOWNLOAD) {
      length = MAX_NUMBER_OF_AUDIOS_TO_DOWNLOAD;
    } else {
      length = allAudios.size();
    }
    List<Audio> extracted = new ArrayList<>();
    for(int i = 0; i < length; i++) {
      Audio audio = allAudios.get(i);
      if (isAudioMustBeDownloaded(audio)) {
        extracted.add(audio);
      }
    }

    return extracted;
  }

  private boolean isAudioMustBeDownloaded(Audio audio) {
    return audio.isAvailableForDownload() && !audio.isDownloaded();
  }
}
