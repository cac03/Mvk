package com.caco3.mvk.audios;

import com.caco3.mvk.mvp.BasePresenter;
import com.caco3.mvk.vk.audio.Audio;

public interface MyAudiosPresenter extends BasePresenter<MyAudiosView> {
  void onDownloadRequest(Audio audio);
  void onRefreshRequest();
}
