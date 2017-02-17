package com.caco3.mvk.vk.audio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class AudioParseTest {
  private static final String VALID_JSON = "{\"id\":3,\"owner_id\":4,\"artist\":\"CamelPhat\",\"title\":\"The Act (Extended Mix)\",\"duration\":495,\"date\":12,\"url\":\"https://figvam.ru\",\"lyrics_id\":8,\"genre_id\":5}";
  private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
  private final Audio audio = gson.fromJson(VALID_JSON, Audio.class);

  @Test
  public void idCorrectlyParsed() {
    assertThat(audio.getId())
            .isEqualTo(3);
  }

  @Test
  public void ownerIdCorrectlyParsed() {
    assertThat(audio.getOwnerId())
            .isEqualTo(4);
  }

  @Test
  public void artistCorrectlyParsed() {
    assertThat(audio.getArtist())
            .isEqualTo("CamelPhat");
  }

  @Test
  public void titleCorrectlyParsed() {
    assertThat(audio.getTitle())
            .isEqualTo("The Act (Extended Mix)");
  }

  @Test
  public void durationCorrectlyParsed() {
    assertThat(audio.getDurationSeconds())
            .isEqualTo(495);
  }

  @Test
  public void urlCorrectlyParsed() {
    assertThat(audio.getDownloadUrl())
            .isEqualTo("https://figvam.ru");
  }
}
