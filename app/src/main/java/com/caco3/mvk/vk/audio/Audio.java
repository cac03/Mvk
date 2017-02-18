package com.caco3.mvk.vk.audio;

import com.caco3.mvk.util.greendao.FileConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.File;
import java.io.Serializable;

/**
 * @see <a href="https://vk.com/dev/objects/audio">Audio object</a>
 */
@Entity
public class Audio implements Cloneable, Serializable {
  @Id
  private Long entityId;
  @SerializedName("artist")
  @Expose
  private String artist;
  @SerializedName("title")
  @Expose
  private String title;
  @SerializedName("duration")
  @Expose
  private int durationSeconds;
  @SerializedName("url")
  @Expose
  private String downloadUrl;
  @SerializedName("id")
  @Expose
  private long id;
  @SerializedName("owner_id")
  @Expose
  private long ownerId;
  private Integer vkPlaylistPosition;
  @Convert(columnType = String.class, converter = FileConverter.class)
  private File file;

  private static final long serialVersionUID = 7998765258689575597L;

  @Generated(hash = 1069985937)
  public Audio(Long entityId, String artist, String title, int durationSeconds, String downloadUrl, long id, long ownerId,
          Integer vkPlaylistPosition, File file) {
      this.entityId = entityId;
      this.artist = artist;
      this.title = title;
      this.durationSeconds = durationSeconds;
      this.downloadUrl = downloadUrl;
      this.id = id;
      this.ownerId = ownerId;
      this.vkPlaylistPosition = vkPlaylistPosition;
      this.file = file;
  }

@Generated(hash = 1642629471)
  public Audio() {
  }

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
    return file != null && file.exists();
  }

  public long getId() {
      return this.id;
  }

  public void setId(Long id) {
      this.id = id;
  }

  public void setArtist(String artist) {
      this.artist = artist;
  }

  public void setTitle(String title) {
      this.title = title;
  }

  public void setDurationSeconds(int durationSeconds) {
      this.durationSeconds = durationSeconds;
  }

  public void setDownloadUrl(String downloadUrl) {
      this.downloadUrl = downloadUrl;
  }

  public boolean isAvailableForDownload() {
    return downloadUrl != null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Audio audio = (Audio) o;

    if (durationSeconds != audio.durationSeconds) return false;
    if (id != audio.id) return false;
    if (ownerId != audio.ownerId) return false;
    if (file != null ? !file.equals(audio.file) : audio.file != null) return false;
    if (artist != null ? !artist.equals(audio.artist) : audio.artist != null) return false;
    if (title != null ? !title.equals(audio.title) : audio.title != null) return false;
    if (downloadUrl != null ? !downloadUrl.equals(audio.downloadUrl) : audio.downloadUrl != null)
      return false;
    return vkPlaylistPosition != null ? vkPlaylistPosition.equals(audio.vkPlaylistPosition) : audio.vkPlaylistPosition == null;

  }

  @Override
  public int hashCode() {
    int result = artist != null ? artist.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + durationSeconds;
    result = 31 * result + (downloadUrl != null ? downloadUrl.hashCode() : 0);
    result = 31 * result + (int) (id ^ (id >>> 32));
    result = 31 * result + (int) (ownerId ^ (ownerId >>> 32));
    result = 31 * result + (file != null ? file.hashCode() : 0);
    result = 31 * result + (vkPlaylistPosition != null ? vkPlaylistPosition.hashCode() : 0);
    return result;
  }

public Long getEntityId() {
    return this.entityId;
}

public void setEntityId(Long entityId) {
    this.entityId = entityId;
}

public void setId(long id) {
    this.id = id;
}

public long getOwnerId() {
    return this.ownerId;
}

public void setOwnerId(long ownerId) {
    this.ownerId = ownerId;
}

public Integer getVkPlaylistPosition() {
    return this.vkPlaylistPosition;
}

public void setVkPlaylistPosition(Integer vkPlaylistPosition) {
    this.vkPlaylistPosition = vkPlaylistPosition;
}

  @Override
  public Audio clone() {
    try {
      return (Audio) super.clone();
    } catch (CloneNotSupportedException cannotHappen) {
      throw new AssertionError(cannotHappen);
    }
  }

  @Override
  public String toString() {
    return artist + " - " + title + " " + durationSeconds + "s. Owner: "
            + ownerId + ". Download url: " + downloadUrl;
  }

  public File getFile() {
      return this.file;
  }

  public void setFile(File file) {
      this.file = file;
  }
}
