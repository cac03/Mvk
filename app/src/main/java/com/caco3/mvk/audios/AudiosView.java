package com.caco3.mvk.audios;


import com.caco3.mvk.mvp.BaseView;
import com.caco3.mvk.vk.audio.Audio;

import java.util.List;

public interface AudiosView extends BaseView<AudiosPresenter> {
  void hideRefreshLayout();
  void showRefreshLayout();
  void showGlobalProgress();
  void hideGlobalProgress();
  void showAudios(List<Audio> audios);
  void showNetworkIsUnavailableError();
  void showNetworkErrorOccurredError();
}
