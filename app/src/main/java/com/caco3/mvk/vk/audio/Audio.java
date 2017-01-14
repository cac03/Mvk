package com.caco3.mvk.vk.audio;

import com.caco3.mvk.data.appuser.AppUser;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.caco3.mvk.vk.auth.DaoSession;
import com.caco3.mvk.data.appuser.AppUserDao;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @see <a href="https://vk.com/dev/objects/audio">Audio object</a>
 */
@Entity
public class Audio {
  @Id
  private Long id;
  private long appUserId;
  @ToOne(joinProperty = "appUserId")
  private AppUser appUser;
  @SerializedName("artist")
  private String artist;
  @SerializedName("title")
  private String title;
  @SerializedName("duration")
  private int durationSeconds;
  @SerializedName("url")
  private String downloadUrl;
  private boolean downloaded;
  /** Used to resolve relations */
  @Generated(hash = 2040040024)
  private transient DaoSession daoSession;
  /** Used for active entity operations. */
  @Generated(hash = 226033729)
  private transient AudioDao myDao;

  @Generated(hash = 839876211)
public Audio(Long id, long appUserId, String artist, String title, int durationSeconds,
        String downloadUrl, boolean downloaded) {
    this.id = id;
    this.appUserId = appUserId;
    this.artist = artist;
    this.title = title;
    this.durationSeconds = durationSeconds;
    this.downloadUrl = downloadUrl;
    this.downloaded = downloaded;
}

@Generated(hash = 1642629471)
  public Audio() {
  }

  @Generated(hash = 1545085567)
private transient Long appUser__resolvedKey;

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

  public Long getId() {
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

  public boolean getDownloaded() {
      return this.downloaded;
  }

  public void setDownloaded(boolean downloaded) {
      this.downloaded = downloaded;
  }

  /** To-one relationship, resolved on first access. */
@Generated(hash = 243680710)
public AppUser getAppUser() {
    long __key = this.appUserId;
    if (appUser__resolvedKey == null || !appUser__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        AppUserDao targetDao = daoSession.getAppUserDao();
        AppUser appUserNew = targetDao.load(__key);
        synchronized (this) {
            appUser = appUserNew;
            appUser__resolvedKey = __key;
        }
    }
    return appUser;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1485078859)
public void setAppUser(@NotNull AppUser appUser) {
    if (appUser == null) {
        throw new DaoException(
                "To-one property 'appUserId' has not-null constraint; cannot set to-one to null");
    }
    synchronized (this) {
        this.appUser = appUser;
        appUserId = appUser.getId();
        appUser__resolvedKey = appUserId;
    }
}

/**
   * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
   * Entity must attached to an entity context.
   */
  @Generated(hash = 128553479)
  public void delete() {
      if (myDao == null) {
          throw new DaoException("Entity is detached from DAO context");
      }
      myDao.delete(this);
  }

  /**
   * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
   * Entity must attached to an entity context.
   */
  @Generated(hash = 1942392019)
  public void refresh() {
      if (myDao == null) {
          throw new DaoException("Entity is detached from DAO context");
      }
      myDao.refresh(this);
  }

  /**
   * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
   * Entity must attached to an entity context.
   */
  @Generated(hash = 713229351)
  public void update() {
      if (myDao == null) {
          throw new DaoException("Entity is detached from DAO context");
      }
      myDao.update(this);
  }

  /** called by internal mechanisms, do not call yourself. */
  @Generated(hash = 1261206123)
  public void __setDaoSession(DaoSession daoSession) {
      this.daoSession = daoSession;
      myDao = daoSession != null ? daoSession.getAudioDao() : null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Audio audio = (Audio) o;

    if (durationSeconds != audio.durationSeconds) return false;
    if (downloaded != audio.downloaded) return false;
    if (artist != null ? !artist.equals(audio.artist) : audio.artist != null) return false;
    if (title != null ? !title.equals(audio.title) : audio.title != null) return false;
    return downloadUrl != null ? downloadUrl.equals(audio.downloadUrl) : audio.downloadUrl == null;

  }

  @Override
  public int hashCode() {
    int result = artist != null ? artist.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + durationSeconds;
    result = 31 * result + (downloadUrl != null ? downloadUrl.hashCode() : 0);
    result = 31 * result + (downloaded ? 1 : 0);
    return result;
  }

public long getAppUserId() {
    return this.appUserId;
}

public void setAppUserId(long appUserId) {
    this.appUserId = appUserId;
}
}
