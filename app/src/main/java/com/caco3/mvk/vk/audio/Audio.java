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

/**
 * @see <a href="https://vk.com/dev/objects/audio">Audio object</a>
 */
@Entity
public class Audio {
  @Id
  private Long id;
  @ToOne
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

  @Generated(hash = 1560625128)
  public Audio(Long id, String artist, String title, int durationSeconds,
          String downloadUrl, boolean downloaded) {
      this.id = id;
      this.artist = artist;
      this.title = title;
      this.durationSeconds = durationSeconds;
      this.downloadUrl = downloadUrl;
      this.downloaded = downloaded;
  }

  @Generated(hash = 1642629471)
  public Audio() {
  }

  @Generated(hash = 1660794392)
  private transient boolean appUser__refreshed;

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
  @Generated(hash = 1234809772)
  public AppUser getAppUser() {
      if (appUser != null || !appUser__refreshed) {
          if (daoSession == null) {
              throw new DaoException("Entity is detached from DAO context");
          }
          AppUserDao targetDao = daoSession.getAppUserDao();
          targetDao.refresh(appUser);
          appUser__refreshed = true;
      }
      return appUser;
  }

  /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
  @Generated(hash = 1922394157)
  public AppUser peakAppUser() {
      return appUser;
  }

  /** called by internal mechanisms, do not call yourself. */
  @Generated(hash = 1104270068)
  public void setAppUser(AppUser appUser) {
      synchronized (this) {
          this.appUser = appUser;
          appUser__refreshed = true;
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
}
