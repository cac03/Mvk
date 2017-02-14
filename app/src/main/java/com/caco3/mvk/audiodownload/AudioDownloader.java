package com.caco3.mvk.audiodownload;


import com.caco3.mvk.vk.audio.Audio;

public interface AudioDownloader {
  void post(Audio audio);
}
