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
  private MyAudiosView view;
  private AppUser currentAppUser;
  private AudiosRepository audiosRepository;
  private Vk vk;
  private Subscriber<List<Audio>> vkAudiosSubscriber = null;
  private AudioDownloadPresenter audioDownloadPresenter;
  private DataSetFilter<Audio> audiosFilter;

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
    if (isViewAttached()) {
      view.showGlobalProgress();
    }
    loadAudiosFromRepositoryToView();
  }

  private void loadAudiosFromRepositoryToView() {
    Observable.fromCallable(new Callable<List<Audio>>() {
      @Override
      public List<Audio> call() {
        return audiosRepository.getAllByAppUser(currentAppUser);
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<Audio>>() {
              @Override
              public void call(List<Audio> audios) {
                if (isViewAttached()) {
                  view.showAudios(audios);
                  resetOrInitAudiosFilter(audios);
                  view.hideGlobalProgress();
                }
              }
            });
  }

  private void resetOrInitAudiosFilter(List<Audio> audios) {
    if (audiosFilter == null) {
      audiosFilter = new AudiosFilter(audios);
    } else {
      audiosFilter.resetWith(audios);
    }
  }

  private boolean isViewAttached() {
    return view != null;
  }

  @Override
  public void onViewDetached(MyAudiosView view) {
    this.view = null;
  }

  @Override
  public void onRefreshRequest() {
    loadAudiosFromVk();
  }

  private void loadAudiosFromVk() {
    if (isViewAttached()) {
      view.showRefreshLayout();
    }
    vkAudiosSubscriber = new VkAudiosSubscriber();
    Observable.fromCallable(new Callable<List<Audio>>() {
      @Override
      public List<Audio> call() throws Exception {
        List<Audio> audios = vk.audios().get(currentAppUser.getUserToken());
        for(Audio audio : audios) {
          audio.setAppUser(currentAppUser);

        }
        audiosRepository.deleteAllByAppUser(currentAppUser);
        audiosRepository.saveAll(audios);
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
      if (isViewAttached()) {
        view.hideRefreshLayout();
      }
      Timber.e(e, "An error occurred while trying to get audios (username = '%s')",
              currentAppUser.getUsername());
      if (e instanceof IOException) {
        Timber.e("No network?");
        if (isViewAttached()) {
          view.showNetworkErrorOccurredError();
        }
      } else if (e instanceof VkException) {
        Timber.e("Unknown subclass of vk exception caught.");
      } else {
        throw Exceptions.propagate(e);
      }
    }

    @Override
    public void onNext(List<Audio> audios) {
      vkAudiosSubscriber = null;
      resetOrInitAudiosFilter(audios);
      if (isViewAttached()) {
        view.hideRefreshLayout();
        view.showAudios(audios);
      }
    }
  }

  @Override
  public void onDownloadRequest(Audio audio) {
    audioDownloadPresenter.startDownload(audio);
  }

  @Override
  public void onSearch(String query) {
    List<Audio> lastAudiosReturnedByFilter = audiosFilter.filter(query);
    if (isViewAttached()) {
      view.showAudios(lastAudiosReturnedByFilter);
    }
  }

  @Override
  public void onSearchCanceled() {
    if (isViewAttached()) {
      loadAudiosFromRepositoryToView();
    }
  }
}
