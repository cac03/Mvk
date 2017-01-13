package com.caco3.mvk.vk.audio;

import com.google.gson.annotations.SerializedName;

/**
 * @see <a href="https://vk.com/dev/objects/audio">Audio object</a>
 */
public class Audio {
  @SerializedName("artist")
  private String artist;
  @SerializedName("title")
  private String title;
  @SerializedName("duration")
  private int durationSeconds;
  @SerializedName("url")
  private String downloadUrl;
  private boolean downloaded;

  public String getArtist() {
    return artist;
  }

  public String getTitle() {
    return title;
  }

  public int getDurationSeconds() {
    return durationSeconds;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public boolean isDownloaded() {
    return downloaded;
  }
}
