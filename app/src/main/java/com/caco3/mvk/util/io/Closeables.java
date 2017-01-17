package com.caco3.mvk.util.io;

import java.io.Closeable;
import java.io.IOException;

import timber.log.Timber;


public class Closeables {
  private Closeables(){
  }

  /**
   * Tries to close a {@link Closeable}, ignores {@link IOException} if thrown,
   * other exceptions will not be caught
   */
  public static void closeSilently(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException ignore) {
      }
    }
  }

  /**
   * Tries to close a {@link Closeable}, if {@link IOException} thrown, logs it
   */
  public static void closeOrLog(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        Timber.e(e, "Unable to close closeable (%s)", closeable);
      }
    }
  }
}
