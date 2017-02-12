package com.caco3.mvk.util;


import java.util.Iterator;

public class Strings {
  private Strings(){
  }

  /**
   * Throws {@link IllegalArgumentException} with {@code msg} if provided string is null or empty
   */
  public static String requireNotNullAndNotEmpty(String s, String msg) {
    if (s == null || s.isEmpty()) {
      throw new IllegalArgumentException(msg);
    } else {
      return s;
    }
  }

  /**
   * Throws {@link IllegalArgumentException} if provided string is null or empty
   */
  public static String requireNotNullAndNotEmpty(String s) {
    return requireNotNullAndNotEmpty(s, null);
  }

  public static String join(CharSequence delimiter, long[] longs) {
    StringBuilder sb = new StringBuilder();
    for(int i = 0, length = longs.length; i < length - 1; i++) {
      sb.append(longs[i])
              .append(delimiter);
    }

    if (longs.length > 0) {
      sb.append(longs[longs.length - 1]);
    }

    return sb.toString();
  }
}
