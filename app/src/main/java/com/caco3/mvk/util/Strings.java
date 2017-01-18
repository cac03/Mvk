package com.caco3.mvk.util;


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
}
