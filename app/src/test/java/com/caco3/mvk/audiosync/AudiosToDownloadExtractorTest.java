package com.caco3.mvk.audiosync;

import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.AudiosGenerator;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AudiosToDownloadExtractorTest {
  private AudiosGenerator audiosGenerator = new AudiosGenerator();
  private AudiosToDownloadExtractor extractor = new AudiosToDownloadExtractor();

  @Test public void extractCalled_extracted20AudiosAtMost() {
    List<Audio> audios = audiosGenerator.generateList(10000);
    assertThat(extractor.extract(audios))
            .hasSize(20);
  }

  @Test public void alreadyDownloadedAudiosAreNotExtracted() {
    // TODO: 2/19/17 audiosGenerator.generateDownloaded();
    Audio notExtracted = audiosGenerator.generateOne();
    File mockFile = mock(File.class);
    when(mockFile.exists()).thenReturn(true);
    notExtracted.setFile(mockFile);

    Audio extracted = audiosGenerator.generateOne();
    List<Audio> extractFrom = Arrays.asList(notExtracted, extracted);
    assertThat(extractor.extract(extractFrom))
            .hasSize(1)
            .contains(extracted)
            .doesNotContain(notExtracted);
  }
}
