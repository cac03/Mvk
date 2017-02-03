package com.caco3.mvk.audiodownload;

import com.caco3.mvk.vk.audio.AudiosGenerator;
import com.caco3.mvk.vk.audio.Audio;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class MvkAudioFileTest {
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  private final AudiosGenerator audiosGenerator = new AudiosGenerator();

  @Test
  public void prepareForDownloadCalled_temporaryFileCreated() throws Exception {
    Audio audio = audiosGenerator.generateAudio();
    MvkAudioFile audioFile = new MvkAudioFile(temporaryFolder.getRoot(), audio);

    assertTrue(audioFile.prepareForDownload().exists());
  }

  @Test
  public void restoreAfterDownloadCalled_fileRenamedWithExpectedExtension() throws Exception {
    String expected = "mp3";
    Audio audio = audiosGenerator.generateAudio();
    audio.setDownloadUrl(audio.getDownloadUrl() + "." + expected + "?dummy=asdasd&param=param");
    MvkAudioFile audioFile = new MvkAudioFile(temporaryFolder.getRoot(), audio);
    audioFile.prepareForDownload();

    String fileName = audioFile.restoreAfterDownload().getName();
    String actual = fileName.substring(fileName.length() - 3, fileName.length());
    assertEquals(expected, actual);
  }

  @Test
  public void restoreAfterDownloadCalledAndUrlHasNoGetParameters_fileRenamedWithExpectedExtension()
    throws Exception {
    String expected = "mp3";
    Audio audio = audiosGenerator.generateAudio();
    audio.setDownloadUrl(audio.getDownloadUrl() + "." + expected);
    MvkAudioFile audioFile = new MvkAudioFile(temporaryFolder.getRoot(), audio);
    audioFile.prepareForDownload();

    String fileName = audioFile.restoreAfterDownload().getName();
    String actual = fileName.substring(fileName.length() - 3, fileName.length());
    assertEquals(expected, actual);
  }
}
