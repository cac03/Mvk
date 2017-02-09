package com.caco3.mvk.data.audio;

import com.caco3.mvk.data.BaseRepository;
import com.caco3.mvk.vk.audio.Audio;

import java.util.List;

public interface AudiosRepository extends BaseRepository<Audio> {
  List<Audio> getAllByVkUserId(long vkUserId);
  void deleteAllByVkUserId(long vkUserId);
  void replaceAllByVkUserId(long vkUserId, Iterable<Audio> audios);
}
