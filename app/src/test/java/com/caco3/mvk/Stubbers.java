package com.caco3.mvk;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.doAnswer;

public class Stubbers {
  private Stubbers() {
    throw new AssertionError("No instances");
  }

  public static Stubber setTrue(final AtomicBoolean atomicBoolean) {
    return doAnswer(new Answer<Object>(){
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        atomicBoolean.set(true);
        return null;
      }
    });
  }

  public static <T> Stubber appendTo(final Collection<T> collection, final int argIndex) {
    return doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        // noinspection unchecked
        collection.add((T)invocation.getArguments()[argIndex]);
        return null;
      }
    });
  }

  public static <T> Stubber appendTo(Collection<T> collection) {
    return appendTo(collection, 0);
  }

  public static <T> Stubber setArg(final AtomicReference<T> reference, final int argIndex) {
    return doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        // noinspection unchecked
        reference.set((T)invocation.getArguments()[argIndex]);
        return null;
      }
    });
  }

  public static <T> Stubber setArg(AtomicReference<T> reference) {
    return setArg(reference, 0);
  }
}
