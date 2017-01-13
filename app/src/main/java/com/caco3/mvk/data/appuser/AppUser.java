package com.caco3.mvk.data.appuser;


import com.caco3.mvk.vk.auth.UserToken;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.caco3.mvk.vk.auth.DaoSession;
import com.caco3.mvk.vk.auth.UserTokenDao;

@Entity
public class AppUser implements Serializable {
  @Id
  private Long id;
  @ToOne
  private UserToken userToken;
  private String username;

  private static final long serialVersionUID = 797454612133L;
  /** Used to resolve relations */
  @Generated(hash = 2040040024)
  private transient DaoSession daoSession;
  /** Used for active entity operations. */
  @Generated(hash = 1682778229)
  private transient AppUserDao myDao;
  @Generated(hash = 349085159)
  private transient boolean userToken__refreshed;

  @Keep
  public AppUser(UserToken userToken, String username) {
    this.userToken = userToken;
    this.username = username;
  }


  @Generated(hash = 716408921)
  public AppUser(Long id, String username) {
      this.id = id;
      this.username = username;
  }


  @Generated(hash = 70494256)
  public AppUser() {
  }
  

  public String getUsername() {
    return username;
  }


  public Long getId() {
      return this.id;
  }


  public void setId(Long id) {
      this.id = id;
  }


  public void setUsername(String username) {
      this.username = username;
  }


  /** To-one relationship, resolved on first access. */
  @Generated(hash = 1329153652)
  public UserToken getUserToken() {
      if (userToken != null || !userToken__refreshed) {
          if (daoSession == null) {
              throw new DaoException("Entity is detached from DAO context");
          }
          UserTokenDao targetDao = daoSession.getUserTokenDao();
          targetDao.refresh(userToken);
          userToken__refreshed = true;
      }
      return userToken;
  }


  /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
  @Generated(hash = 1534830427)
  public UserToken peakUserToken() {
      return userToken;
  }


  /** called by internal mechanisms, do not call yourself. */
  @Generated(hash = 1947692991)
  public void setUserToken(UserToken userToken) {
      synchronized (this) {
          this.userToken = userToken;
          userToken__refreshed = true;
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
  @Generated(hash = 34658731)
  public void __setDaoSession(DaoSession daoSession) {
      this.daoSession = daoSession;
      myDao = daoSession != null ? daoSession.getAppUserDao() : null;
  }
}
