package com.caco3.mvk.data.appuser;

import com.caco3.mvk.data.BaseRepository;


public interface AppUsersRepository extends BaseRepository<AppUser> {
  /** Call {@link BaseRepository<AppUser>#save(AppUser)} first */
  void setAsActive(AppUser appUser);
  AppUser getActive();
  boolean hasActiveAppUser();
}
