package com.caco3.mvk.data.appuser;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.List;

import static com.caco3.mvk.util.Preconditions.checkNotNull;


public class AppUsersRepositoryImpl implements AppUsersRepository {
  private static final String SHARED_PREFERENCES_FILENAME = "users";
  private static final String ACTIVE_APP_USER_ID_KEY = "active";

  private final SharedPreferences preferences;
  private final AppUserDao dao;

  public AppUsersRepositoryImpl(Context context, AppUserDao dao) {
    this.dao = checkNotNull(dao);
    this.preferences = context.getSharedPreferences(SHARED_PREFERENCES_FILENAME,
            Context.MODE_PRIVATE);
  }

  @Override
  public void save(AppUser appUser) {
    dao.save(appUser);
  }

  @Override
  public List<AppUser> getAll() {
    return dao.loadAll();
  }

  @Override
  public void update(AppUser appUser) {
    dao.update(appUser);
  }

  @Override
  public void delete(AppUser appUser) {
    dao.delete(appUser);
  }

  @Override
  public void deleteAll() {
    dao.deleteAll();
  }

  @Override
  public void setAsActive(AppUser appUser) {
    preferences.edit().putLong(ACTIVE_APP_USER_ID_KEY, appUser.getId()).apply();
  }

  @Override
  public AppUser getActive() {
    long activeAppUserId = getActiveAppUserId();
    return dao.load(activeAppUserId);
  }

  private long getActiveAppUserId() {
    return preferences.getLong(ACTIVE_APP_USER_ID_KEY, -1);
  }

  @Override
  public boolean hasActiveAppUser() {
    return preferences.contains(ACTIVE_APP_USER_ID_KEY);
  }

  @Override
  public void saveAll(Iterable<AppUser> entities) {
    dao.saveInTx(entities);
  }

  @Override
  public void updateAll(Iterable<AppUser> entities) {
    dao.updateInTx(entities);
  }
}
