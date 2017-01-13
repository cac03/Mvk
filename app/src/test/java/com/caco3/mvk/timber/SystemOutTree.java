package com.caco3.mvk.timber;


import timber.log.Timber;

public class SystemOutTree extends Timber.DebugTree {
  @Override
  protected void log(int priority, String tag, String message, Throwable t) {
    System.out.println(String.format("%s: %s", tag, message));
    if (t != null) {
      t.printStackTrace();
    }
  }
}
