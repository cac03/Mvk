package com.caco3.mvk.myaudios;


import com.caco3.mvk.audiodownload.AudioDownloadPresenter;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.search.DataSetFilter;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.VkException;
import com.caco3.mvk.vk.audio.Audio;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/*package*/ class MyAudiosPresenterImpl implements MyAudiosPresenter {
  private MyAudiosView view = NullAudiosView.INSTANCE;
  private AppUser currentAppUser;
  private AudiosRepository audiosRepository;
  private Vk vk;
  private Subscriber<List<Audio>> vkAudiosSubscriber = null;
  private AudioDownloadPresenter audioDownloadPresenter;
  private DataSetFilter<Audio> audiosFilter = new AudiosFilter();
  private List<Audio> cachedAudios;
  private String searchQuery = "";

  @Inject
  /*package*/ MyAudiosPresenterImpl(AppUser appUser, AudiosRepository audiosRepository,
                                    Vk vk, AudioDownloadPresenter audioDownloadPresenter) {
    this.currentAppUser = appUser;
    this.audiosRepository = audiosRepository;
    this.vk = vk;
    this.audioDownloadPresenter = audioDownloadPresenter;
  }

  @Override
  public void onViewAttached(MyAudiosView view) {
    this.view = view;
    initView();
  }

  private void initView() {
    view.showGlobalProgress();
    if (areAudiosLoadingFromVk()) {
      view.showRefreshLayout();
    }
    showAudios();
  }

  private boolean areAudiosLoadingFromVk() {
    return vkAudiosSubscriber != null && !vkAudiosSubscriber.isUnsubscribed();
  }

  private void showAudios() {
    if (areAudiosCached()) {
      if (isSearching()) {
        view.showAudios(audiosFilter.filter(cachedAudios, searchQuery));
      } else {
        view.showAudios(cachedAudios);
      }
      view.hideGlobalProgress();
    } else {
      loadAudiosFromRepository();
    }
  }

  private boolean areAudiosCached() {
    return cachedAudios != null;
  }

  private boolean isSearching() {
    return searchQuery != null && !searchQuery.isEmpty();
  }

  private void loadAudiosFromRepository() {
    Observable.fromCallable(new Callable<List<Audio>>() {
      @Override
      public List<Audio> call() {
        return audiosRepository.getAllByVkUserId(currentAppUser.getUserToken().getVkUserId());
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<Audio>>() {
              @Override
              public void call(List<Audio> audios) {
                setCache(audios);
                showAudios();
                view.hideGlobalProgress();
              }
            });
  }

  private void setCache(List<Audio> audios) {
    cachedAudios = audios;
  }

  @Override
  public void onViewDetached(MyAudiosView view) {
    this.view = NullAudiosView.INSTANCE;
  }

  @Override
  public void onRefreshRequest() {
    loadAudiosFromVk();
  }

  private void loadAudiosFromVk() {
    view.showRefreshLayout();
    vkAudiosSubscriber = new VkAudiosSubscriber();
    Observable.fromCallable(new Callable<List<Audio>>() {
      @Override
      public List<Audio> call() throws Exception {
        List<Audio> audios = vk.audios().get();

        audiosRepository.replaceAllByVkUserId(currentAppUser.getUserToken().getVkUserId(),
                audios);
        return audios;
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(vkAudiosSubscriber);
  }

  private class VkAudiosSubscriber extends Subscriber<List<Audio>> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
      vkAudiosSubscriber = null;
      view.hideRefreshLayout();
      Timber.e(e, "An error occurred while trying to get audios (username = '%s')",
              currentAppUser.getUsername());
      if (e instanceof IOException) {
        Timber.e("No network?");
        view.showNetworkErrorOccurredError();
      } else if (e instanceof VkException) {
        Timber.e("Unknown subclass of vk exception caught.");
      } else {
        throw Exceptions.propagate(e);
      }
    }

    @Override
    public void onNext(List<Audio> audios) {
      vkAudiosSubscriber = null;
      setCache(audios);
      view.hideRefreshLayout();
      showAudios();
    }
  }

  @Override
  public void onDownloadRequest(Audio audio) {
    audioDownloadPresenter.startDownload(audio);
  }

  @Override
  public void onSearch(String query) {
    searchQuery = query;
    showAudios();
  }

  @Override
  public void onSearchCanceled() {
    searchQuery = "";
    showAudios();
  }
}
