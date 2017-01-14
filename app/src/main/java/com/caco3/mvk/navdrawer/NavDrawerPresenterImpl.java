package com.caco3.mvk.navdrawer;

import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.appuser.AppUsersRepository;
import com.caco3.mvk.data.vkuser.VkUsersRepository;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.users.VkUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/*package*/ class NavDrawerPresenterImpl implements NavDrawerPresenter {
  private static final long MIN_TIME_DIFF_TO_UPDATE_CURRENT_VK_USER
          = TimeUnit.HOURS.toMillis(3);

  private List<NavDrawerView> views = new ArrayList<>(2);
  private AppUsersRepository appUsersRepository;
  private AppUser currentAppUser;
  private VkUsersRepository vkUsersRepository;
  private Vk vk;
  private Subscriber<VkUser> vkUserSubscriber = null;

  /*package*/ NavDrawerPresenterImpl(AppUsersRepository appUsersRepository,
                                AppUser appUser, VkUsersRepository vkUsersRepository, Vk vk){
    this.appUsersRepository = appUsersRepository;
    this.currentAppUser = appUser;
    this.vkUsersRepository = vkUsersRepository;
    this.vk = vk;
  }

  @Override
  public void onViewAttached(NavDrawerView view) {
    views.add(view);
    initView(view);
  }

  private void initView(NavDrawerView view) {
    if (needToUpdateVkUser() && !isVkUserUpdating()) {
      updateVkUser();
    } else {
      view.showVkUser(currentAppUser.getVkUser());
    }
  }

  private boolean isVkUserUpdating() {
    return vkUserSubscriber != null && !vkUserSubscriber.isUnsubscribed();
  }

  private boolean needToUpdateVkUser() {
    VkUser vkUser = currentAppUser.getVkUser();
    return vkUser == null || System.currentTimeMillis() - vkUser.getLastUpdated()
            >= MIN_TIME_DIFF_TO_UPDATE_CURRENT_VK_USER;
  }

  private void updateVkUser() {
    vkUserSubscriber = new VkUserSubscriber();
    Observable.fromCallable(new Callable<VkUser>() {
      @Override
      public VkUser call() throws Exception {
        VkUser vkUser = vk.users().get(currentAppUser.getUserToken());
        vkUser.setLastUpdated(System.currentTimeMillis());
        vkUsersRepository.save(vkUser);
        currentAppUser.setVkUser(vkUser);
        appUsersRepository.update(currentAppUser);

        return vkUser;
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(vkUserSubscriber);
  }

  @Override
  public void onViewDetached(NavDrawerView view) {
    views.remove(view);
  }

  private class VkUserSubscriber extends Subscriber<VkUser> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
      Timber.e(e, "An error occurred while trying to get current VkUser");
    }

    @Override
    public void onNext(VkUser vkUser) {
      vkUserSubscriber = null;
      System.out.println("Successfully received VkUser");
      for(NavDrawerView navDrawerView : views) {
        navDrawerView.showVkUser(vkUser);
      }
    }
  }
}
