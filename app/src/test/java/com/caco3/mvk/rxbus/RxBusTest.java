package com.caco3.mvk.rxbus;

import org.junit.Test;

import rx.observers.TestSubscriber;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RxBusTest {
  private RxBus rxBus = RxBus.getInstance();
  @Test
  public void postingOneEvent_eventReceivedInAllSubscribers() {
    Object event = new Object();
    TestSubscriber<Object> testSubscriber1 = new TestSubscriber<>();
    TestSubscriber<Object> testSubscriber2 = new TestSubscriber<>();
    rxBus.observable().subscribe(testSubscriber1);
    rxBus.observable().subscribe(testSubscriber2);
    rxBus.post(event);

    assertThat(testSubscriber1.getOnNextEvents())
            .hasSize(1)
            .contains(event);
    assertThat(testSubscriber2.getOnNextEvents())
            .hasSize(1)
            .contains(event);
  }

  @Test(expected = NullPointerException.class)
  public void nullEventPosted_npeThrown() {
    rxBus.post(null);
  }

  @Test
  public void getInstanceAlwaysReturnsTheSameInstance() {
    RxBus bus = RxBus.getInstance();
    for(int i = 0; i < 10; i++) {
      assertThat(RxBus.getInstance())
              .isSameAs(bus);
    }
  }
}
