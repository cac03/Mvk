package com.caco3.mvk.data.audio;

import com.caco3.mvk.data.BaseRepository;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.vk.audio.Audio;

import java.util.List;

public interface AudiosRepository extends BaseRepository<Audio> {
  List<Audio> getAllByAppUser(AppUser appUser);
  void deleteAllByAppUser(AppUser appUser);
}
