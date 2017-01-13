package com.caco3.mvk.login;

import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.appuser.AppUsersRepository;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.VkException;
import com.caco3.mvk.vk.auth.Credentials;
import com.caco3.mvk.vk.auth.UserToken;
import com.caco3.mvk.vk.auth.UsernameOrPasswordIncorrectException;

import java.io.IOException;
import java.util.concurrent.Callable;


import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/*package*/ class LogInPresenterImpl implements LogInPresenter {
  private LogInView view;
  private Vk vk;
  private AppUsersRepository appUsersRepository;
  private Subscriber<UserToken> userTokenSubscriber = null;

  /*package*/LogInPresenterImpl(Vk vk, AppUsersRepository appUsersRepository) {
    this.vk = vk;
    this.appUsersRepository = appUsersRepository;
  }

  @Override
  public void onViewAttached(LogInView view) {
    this.view = view;
    initView();
  }

  private void initView() {
    if (isViewAttached()) {
      if (userTokenSubscriber != null && !userTokenSubscriber.isUnsubscribed()) {
        view.showLogInProgress();
      }
    }
  }

  private boolean isViewAttached() {
    return view != null;
  }

  @Override
  public void onViewDetached(LogInView view) {
    this.view = null;
  }

  @Override
  public void attemptToLogIn(String username, String password) {
    boolean cancel = false;
    if (!isUsernameValid(username)) {
      if (isViewAttached()) {
        view.showUsernameIsEmptyStringError();
      }
      cancel = true;
    }

    if (!isPasswordValid(password)) {
      if (isViewAttached()) {
        view.showPasswordIsEmptyStringError();
      }
      cancel = true;
    }

    if (cancel) {
      return;
    }
    userTokenSubscriber = new UserTokenSubscriber(username);
    final Credentials credentials = new Credentials(username, password);
    Observable.fromCallable(new Callable<UserToken>() {
      @Override
      public UserToken call() throws Exception {
        return vk.auth().getUserToken(credentials);
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(userTokenSubscriber);

  }

  private static boolean isUsernameValid(String username) {
    return !username.isEmpty();
  }

  private static boolean isPasswordValid(String password) {
    return !password.isEmpty();
  }

  @Override
  public void cancelLoggingIn() {

  }

  private class UserTokenSubscriber extends Subscriber<UserToken> {
    private String username;

    public UserTokenSubscriber(String username) {
      this.username = username;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
      userTokenSubscriber = null;
      Timber.e(e, "An error occurred while trying to receive user token (username = '%s')", username);
      if (isViewAttached()) {
        view.hideLogInProgress();
      }
      if (e instanceof IOException) {
        if (isViewAttached()) {
          view.showNetworkErrorOccurredError();
        }
      } else if (e instanceof UsernameOrPasswordIncorrectException) {
        if (isViewAttached()) {
          view.showUsernameOrPasswordIncorrectError();
        }
      } else if (e instanceof VkException) {
        Timber.e("Unknown subclass of VkException caught");
      } else {
        throw Exceptions.propagate(e);
      }
    }

    @Override
    public void onNext(UserToken userToken) {
      userTokenSubscriber = null;
      Timber.i("Successfully received userToken (username = '%s')", username);
      AppUser appUser = new AppUser(userToken, username);
      appUsersRepository.save(appUser);
      appUsersRepository.setAsActive(appUser);
      if (isViewAttached()) {
        view.navigateToAudiosActivity();
      }
    }
  }
}
