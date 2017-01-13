package com.caco3.mvk.login;

import com.caco3.mvk.mvp.BaseView;


public interface LogInView extends BaseView<LogInPresenter> {
  void showLogInProgress();
  void hideLogInProgress();
  void showUsernameIsEmptyStringError();
  void showPasswordIsEmptyStringError();
  void showUsernameOrPasswordIncorrectError();
  void showNetworkIsUnavailableError();
  void showNetworkErrorOccurredError();
  void navigateToAudiosActivity();
}
