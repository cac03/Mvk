package com.caco3.mvk.data.appuser;

import java.util.List;


public interface AppUsersRepository {
  void save(AppUser appUser);
  List<AppUser> getAll();
  void update(AppUser appUser);
  void delete(AppUser appUser);
  void removeAll();

  /** Call {@link AppUsersRepository#save(AppUser)} first */
  void setAsActive(AppUser appUser);
  AppUser getActive();
}
