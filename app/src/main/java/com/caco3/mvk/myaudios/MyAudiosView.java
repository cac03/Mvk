package com.caco3.mvk.myaudios;


import com.caco3.mvk.mvp.BaseView;
import com.caco3.mvk.vk.audio.Audio;

import java.util.List;

public interface MyAudiosView extends BaseView<MyAudiosPresenter> {
  void hideRefreshLayout();
  void showRefreshLayout();
  void showGlobalProgress();
  void hideGlobalProgress();
  void showAudios(List<Audio> audios);
  void showNetworkIsUnavailableError();
  void showNetworkErrorOccurredError();
  void showAudioSelected(Audio audio);
  void cancelAudioSelect(Audio audio);
}
