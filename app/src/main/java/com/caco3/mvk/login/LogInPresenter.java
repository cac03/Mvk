package com.caco3.mvk.login;

import com.caco3.mvk.mvp.BasePresenter;

public interface LogInPresenter extends BasePresenter<LogInView> {
  void attemptToLogIn(String username, String password);
  void cancelLoggingIn();
}
