package com.caco3.mvk.mvp;


public interface BasePresenter<View extends BaseView> {
  void onViewAttached(View view);
  void onViewDetached(View view);
}
