package com.caco3.mvk.myaudios;

import com.caco3.mvk.audiodownload.AudioDownloader;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.vk.Vk;

import dagger.Module;
import dagger.Provides;

@Module
public class MyAudiosModule {
  @Provides
  @MyAudiosScope
  public MyAudiosPresenter provideAudiosPresenter(AppUser appUser, AudiosRepository audiosRepository,
                                                  Vk vk, AudioDownloader audioDownloader) {
    return new MyAudiosPresenterImpl(appUser, audiosRepository, vk, audioDownloader);
  }
}
