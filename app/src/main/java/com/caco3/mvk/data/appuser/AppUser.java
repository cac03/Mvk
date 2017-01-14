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
import com.caco3.mvk.vk.users.VkUser;
import com.caco3.mvk.vk.users.VkUserDao;

@Entity
public class AppUser implements Serializable {
  @Id
  private Long id;
  private Long userTokenId;
  @ToOne(joinProperty = "userTokenId")
  private UserToken userToken;
  private Long vkUserId;
  @ToOne(joinProperty = "vkUserId")
  private VkUser vkUser;
  private String username;

  private static final long serialVersionUID = 797454612133L;
  /**
   * Used to resolve relations
   */
  @Generated(hash = 2040040024)
  private transient DaoSession daoSession;
  /**
   * Used for active entity operations.
   */
  @Generated(hash = 1682778229)
  private transient AppUserDao myDao;
  @Generated(hash = 371602608)
  private transient Long userToken__resolvedKey;
  @Generated(hash = 1065472442)
  private transient Long vkUser__resolvedKey;

  @Keep
  public AppUser(UserToken userToken, String username) {
    this.userToken = userToken;
    this.username = username;
  }


  @Generated(hash = 1369865397)
  public AppUser(Long id, Long userTokenId, Long vkUserId, String username) {
      this.id = id;
      this.userTokenId = userTokenId;
      this.vkUserId = vkUserId;
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


  /**
   * To-one relationship, resolved on first access.
   */
  @Generated(hash = 1797125263)
  public UserToken getUserToken() {
    Long __key = this.userTokenId;
    if (userToken__resolvedKey == null || !userToken__resolvedKey.equals(__key)) {
      final DaoSession daoSession = this.daoSession;
      if (daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      UserTokenDao targetDao = daoSession.getUserTokenDao();
      UserToken userTokenNew = targetDao.load(__key);
      synchronized (this) {
        userToken = userTokenNew;
        userToken__resolvedKey = __key;
      }
    }
    return userToken;
  }


  /**
   * called by internal mechanisms, do not call yourself.
   */
  @Generated(hash = 1603107129)
  public void setUserToken(UserToken userToken) {
    synchronized (this) {
      this.userToken = userToken;
      userTokenId = userToken == null ? null : userToken.getId();
      userToken__resolvedKey = userTokenId;
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


  /**
   * called by internal mechanisms, do not call yourself.
   */
  @Generated(hash = 34658731)
  public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getAppUserDao() : null;
  }


  public Long getUserTokenId() {
    return this.userTokenId;
  }


  public void setUserTokenId(Long userTokenId) {
    this.userTokenId = userTokenId;
  }


  public Long getVkUserId() {
      return this.vkUserId;
  }


  public void setVkUserId(Long vkUserId) {
      this.vkUserId = vkUserId;
  }


  /** To-one relationship, resolved on first access. */
  @Generated(hash = 296510890)
  public VkUser getVkUser() {
      Long __key = this.vkUserId;
      if (vkUser__resolvedKey == null || !vkUser__resolvedKey.equals(__key)) {
          final DaoSession daoSession = this.daoSession;
          if (daoSession == null) {
              throw new DaoException("Entity is detached from DAO context");
          }
          VkUserDao targetDao = daoSession.getVkUserDao();
          VkUser vkUserNew = targetDao.load(__key);
          synchronized (this) {
              vkUser = vkUserNew;
              vkUser__resolvedKey = __key;
          }
      }
      return vkUser;
  }


  /** called by internal mechanisms, do not call yourself. */
  @Generated(hash = 1446452749)
  public void setVkUser(VkUser vkUser) {
      synchronized (this) {
          this.vkUser = vkUser;
          vkUserId = vkUser == null ? null : vkUser.getId();
          vkUser__resolvedKey = vkUserId;
      }
  }
}
