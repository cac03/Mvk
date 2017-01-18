package com.caco3.mvk.audiodownload;

import com.caco3.mvk.vk.audio.Audio;

import java.util.ArrayList;
import java.util.List;

import static com.caco3.mvk.util.Preconditions.checkArgument;
import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class AudioDownloadViewsComposite implements AudioDownloadView {
  final List<AudioDownloadView> views = new ArrayList<>();

  public void add(AudioDownloadView view) {
    checkNotNull(view, "view == null");
    checkArgument(!views.contains(view));
    views.add(view);
  }

  public void remove(AudioDownloadView view) {
    checkArgument(views.remove(view), String.format("view(%s) was not attached", view));
  }

  @Override
  public void showDownloadPending(Audio audio) {
    for (AudioDownloadView view : views) {
      view.showDownloadPending(audio);
    }
  }

  @Override
  public void updateProgress(Audio audio, long bytesDownloaded, long bytesTotal, long nanosElapsed) {
    for (AudioDownloadView view : views) {
      view.updateProgress(audio, bytesDownloaded, bytesTotal, nanosElapsed);
    }
  }

  @Override
  public void showDownloadFailed(Audio audio) {
    for(AudioDownloadView view : views) {
      view.showDownloadFailed(audio);
    }
  }

  @Override
  public void showDownloadSuccessful(Audio audio) {
    for(AudioDownloadView view : views) {
      view.showDownloadSuccessful(audio);
    }
  }

  @Override
  public void showDownloadCanceled(Audio audio) {
    for (AudioDownloadView view : views) {
      view.showDownloadCanceled(audio);
    }
  }
}
