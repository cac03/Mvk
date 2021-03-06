package com.caco3.mvk.data.usertoken;

import com.caco3.mvk.vk.auth.UserToken;
import com.caco3.mvk.vk.auth.UserTokenDao;

import java.util.List;

public class UserTokenRepositoryImpl implements UserTokenRepository {
  private final UserTokenDao dao;

  public UserTokenRepositoryImpl(UserTokenDao dao) {
    this.dao = dao;
  }

  @Override
  public void save(UserToken userToken) {
    dao.save(userToken);
  }

  @Override
  public List<UserToken> getAll() {
    return dao.loadAll();
  }

  @Override
  public void update(UserToken userToken) {
    dao.update(userToken);
  }

  @Override
  public void delete(UserToken userToken) {
    dao.delete(userToken);
  }

  @Override
  public void deleteAll() {
    dao.deleteAll();
  }

  @Override
  public void updateAll(Iterable<UserToken> entities) {
    dao.updateInTx(entities);
  }

  @Override
  public void saveAll(Iterable<UserToken> entities) {
    dao.saveInTx(entities);
  }
}
