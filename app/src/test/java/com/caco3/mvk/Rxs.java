package com.caco3.mvk;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

public class Rxs {
  private Rxs() {
    throw new AssertionError("No instances");
  }

  public static void setUpRx() {
    RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook(){
      @Override
      public Scheduler getMainThreadScheduler() {
        return Schedulers.immediate();
      }
    });
    RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook(){
      @Override
      public Scheduler getComputationScheduler() {
        return Schedulers.immediate();
      }

      @Override
      public Scheduler getIOScheduler() {
        return Schedulers.immediate();
      }
    });
  }

  public static void tearDownRx() {
    RxAndroidPlugins.getInstance().reset();
    RxJavaPlugins.getInstance().reset();
  }
}
