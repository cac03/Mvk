package com.caco3.mvk.vk.audio;

import java.io.IOException;
import java.util.List;

/**
 * @see <a href="https://vk.com/dev/objects/audio">Audio object</a>
 */
public interface VkAudiosService {
  /**
   * Returns list of all audios that current user has
   * @return list of audios
   * @throws IOException if i/o error occurs
   */
  List<Audio> get() throws IOException;
}
