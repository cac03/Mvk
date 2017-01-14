package com.caco3.mvk.audios;

import com.caco3.mvk.mvp.BasePresenter;

public interface AudiosPresenter extends BasePresenter<AudiosView> {
  // void onDownloadRequest(Audio);
  void onRefreshRequest();
}
