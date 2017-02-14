package com.caco3.mvk.util;

import java.util.concurrent.Executor;

public class CurrentThreadExecutor implements Executor {
  @Override
  public void execute(Runnable command) {
    command.run();
  }
}
