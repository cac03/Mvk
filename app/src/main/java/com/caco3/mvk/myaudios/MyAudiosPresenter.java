package com.caco3.mvk.myaudios;

import com.caco3.mvk.mvp.BasePresenter;
import com.caco3.mvk.vk.audio.Audio;

public interface MyAudiosPresenter extends BasePresenter<MyAudiosView> {
  void onDownloadRequest(Audio audio);
  void onRefreshRequest();
  void onSearch(String query);
  void onSearchCanceled();
}
