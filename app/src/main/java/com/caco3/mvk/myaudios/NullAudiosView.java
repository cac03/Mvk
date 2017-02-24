package com.caco3.mvk.myaudios;

import com.caco3.mvk.vk.audio.Audio;

import java.util.List;

/*package*/ class NullAudiosView implements MyAudiosView {
  /*package*/ static final NullAudiosView INSTANCE = new NullAudiosView();

  @Override
  public void hideRefreshLayout() {
  }

  @Override
  public void showRefreshLayout() {
  }

  @Override
  public void showGlobalProgress() {
  }

  @Override
  public void hideGlobalProgress() {
  }

  @Override
  public void showAudios(List<Audio> audios) {
  }

  @Override
  public void showNetworkIsUnavailableError() {
  }

  @Override
  public void showNetworkErrorOccurredError() {
  }

  @Override
  public void showAudioSelected(Audio audio) {
  }

  @Override
  public void cancelAudioSelect(Audio audio) {
  }

  @Override public void startSelectMode() {
  }

  @Override public void finishSelectMode() {
  }

  @Override public void setSelectModeTitle(List<Audio> selectedAudios) {
  }
}
