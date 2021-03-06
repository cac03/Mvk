package com.caco3.mvk.vk.audio;

import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AudioTest {

  @Test
  public void fileNotSet_isDownloadedReturnsFalse() {
    Audio audio = new Audio();
    assertThat(audio.isDownloaded())
            .isFalse();
  }

  @Test
  public void fileNotExists_isDownloadedReturnsFalse() {
    File file = mock(File.class);
    when(file.exists()).thenReturn(false);
    Audio audio = new Audio();
    audio.setFile(file);

    assertThat(audio.isDownloaded())
            .isFalse();
  }

  @Test
  public void fileExists_isDownloadedReturnsTrue() {
    File file = mock(File.class);
    when(file.exists()).thenReturn(true);
    Audio audio = new Audio();
    audio.setFile(file);

    assertThat(audio.isDownloaded())
            .isTrue();
  }

  @Test
  public void downloadUrlIsNull_isAvailableForDownloadReturnsFalse() {
    Audio audio = new Audio();
    assertThat(audio.isAvailableForDownload())
            .isFalse();
  }

  @Test
  public void downloadUrlNotNull_isAvailableForDownloadReturnsTrue() {
    Audio audio = new Audio();
    audio.setDownloadUrl("https://something.com/mp3");
    assertThat(audio.isAvailableForDownload())
            .isTrue();
  }

  @Test public void downloadUrlIsEmptyString_isAvailableForDownloadReturnsFalse() {
    Audio audio = new Audio();
    audio.setDownloadUrl("");
    assertThat(audio.isAvailableForDownload())
            .isFalse();
  }
}
