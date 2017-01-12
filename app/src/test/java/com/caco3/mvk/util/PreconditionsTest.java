package com.caco3.mvk.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class PreconditionsTest {
  private final Object object = new Object();
  private final String message = "something went wrong";

  @Test(expected = NullPointerException.class)
  public void nullProvided_npeThrown() {
    Preconditions.checkNotNull(null);
  }

  @Test
  public void objectProvided_objectReturned() {
    assertTrue(Preconditions.checkNotNull(object).equals(object));
  }

  @Test
  public void nullProvidedWithMessage_messageSet() {
    try {
      Preconditions.checkNotNull(object, message);
    } catch (NullPointerException e) {
      assertEquals(message, e.getMessage());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void falseProvided_illegalArgumentExceptionThrown() {
    Preconditions.checkArgument(false);
  }

  @Test
  public void falseProvidedWithMessage_messageInIllegalArgumentExceptionSet() {
    try {
      Preconditions.checkArgument(false, message);
    } catch (IllegalArgumentException e) {
      assertEquals(message, e.getMessage());
    }
  }

  @Test
  public void trueProvided_IllegalArgumentExceptionNotThrown() {
    Preconditions.checkArgument(true);
  }

  @Test(expected = IllegalStateException.class)
  public void falseProvided_illegalStateExceptionThrown() {
    Preconditions.checkState(false);
  }

  @Test
  public void falseProvidedWithMessage_messageInIllegalStateExceptionSet() {
    try {
      Preconditions.checkState(false, message);
    } catch (IllegalStateException e) {
      assertEquals(message, e.getMessage());
    }
  }

  @Test
  public void trueProvided_IllegalStateExceptionNotThrown() {
    Preconditions.checkState(true);
  }
}
