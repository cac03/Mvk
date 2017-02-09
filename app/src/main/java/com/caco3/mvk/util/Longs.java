package com.caco3.mvk.util;


public class Longs {
  private Longs() {
    throw new AssertionError(String.format("No %s instances", Longs.class.getSimpleName()));
  }

  public static int compare(long a, long b) {
    if (a < b) {
      return -1;
    } else if (a == b) {
      return 0;
    } else {
      return 1;
    }
  }
}
