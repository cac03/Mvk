package com.caco3.mvk.audiodownload;

import com.caco3.mvk.storage.file.AbstractDownloadableMvkFile;
import com.caco3.mvk.vk.audio.Audio;

import java.io.File;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class MvkAudioFile extends AbstractDownloadableMvkFile {
  private static final String TEMPORARY_EXTENSION = "mvkAudio";

  private final Audio audio;

  public MvkAudioFile(File directory, Audio audio) {
    super(directory);
    this.audio = checkNotNull(audio, "audio == null");
  }

  @Override
  protected String generateFileName() {
    return audio.getArtist() + " - " + audio.getTitle();
  }

  @Override
  protected String getRealExtension() {
    return extractExtension();
  }

  private String extractExtension() {
    String url = audio.getDownloadUrl();
    // after an extension there are some GET parameters
    int getParamsStart = url.lastIndexOf('?');
    int endOfExtension = getParamsStart != - 1
            ? getParamsStart : url.length();
    int startOfExtension = url.lastIndexOf('.', endOfExtension);
    if (startOfExtension != -1) {
      return url.substring(startOfExtension + 1, endOfExtension);
    } else {
      throw new IllegalStateException(String
              .format("Unable to extract file extension from url ('%s')", url));
    }
  }

  @Override
  protected String getTemporaryExtension() {
    return TEMPORARY_EXTENSION;
  }
}
