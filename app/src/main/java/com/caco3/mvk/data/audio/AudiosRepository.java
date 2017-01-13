package com.caco3.mvk.data.audio;

import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.vk.audio.Audio;

import java.util.List;

public interface AudiosRepository {
  void save(Audio audio);
  void saveAll(Iterable<Audio> audios);
  List<Audio> getAllByAppUser(AppUser appUser);
  void update(Audio audio);
  void updateAll(Iterable<Audio> audios);
  void delete(Audio audio);
  void deleteAllByAppUser(AppUser appUser);
  void deleteAll();
}
