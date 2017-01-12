package com.caco3.mvk.util;

public class Preconditions {
  private Preconditions(){
    throw new AssertionError("No instances");
  }

  /**
   * Tests whether provided reference is null or not.
   * If reference is null {@link NullPointerException} will be thrown
   *
   * @param reference to test
   * @param message optional message used to construct {@link NullPointerException}
   * @param <T> reference type
   * @return reference
   */
  public static <T> T checkNotNull(T reference, String message) {
    if (reference == null) {
      throw new NullPointerException(message);
    } else {
      return reference;
    }
  }

  /**
   * Tests whether provided reference is null or not.
   * If reference is null {@link NullPointerException} will be thrown
   * @param reference to test
   * @param <T> type of reference
   * @return reference
   */
  public static <T> T checkNotNull(T reference) {
    return checkNotNull(reference, null);
  }

  /**
   * Throws {@link IllegalArgumentException} if provided condition is false.
   * @param condition to test
   * @param message optional message used to construct {@link IllegalArgumentException}
   */
  public static void checkArgument(boolean condition, String message) {
    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Throws {@link IllegalArgumentException} if provided condition is false.
   * @param condition to test
   */
  public static void checkArgument(boolean condition) {
    checkArgument(condition, null);
  }

  /**
   * Throws {@link IllegalStateException} if provided state is false
   * @param state to test
   * @param message used to construct {@link IllegalStateException}
   */
  public static void checkState(boolean state, String message) {
    if (!state) {
      throw new IllegalStateException(message);
    }
  }

  /**
   * Throws {@link IllegalStateException} if provided state is false
   * @param state to test
   */
  public static void checkState(boolean state) {
    checkState(state, null);
  }
}
