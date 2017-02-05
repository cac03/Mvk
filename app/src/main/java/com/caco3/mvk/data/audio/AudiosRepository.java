package com.caco3.mvk.data.audio;

import com.caco3.mvk.data.BaseRepository;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.users.VkUser;

import java.util.List;

public interface AudiosRepository extends BaseRepository<Audio> {
  List<Audio> getAllByVkUserId(long vkUserId);
  void deleteAllByVkUserId(long vkUserId);
}
