package com.caco3.mvk.audiodownload;


import com.caco3.mvk.rxbus.RxBus;
import com.caco3.mvk.vk.audio.Audio;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import rx.schedulers.Schedulers;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

public class AbstractAudioDownloadEventsHandlerTest {
  private final RxBus rxBus = RxBus.getInstance();
  private final AbstractAudioDownloadEventsHandler eventsHandler
          = new AbstractAudioDownloadEventsHandler(rxBus) {
    @Override
    public void handle(AudioAcceptedEvent audioAcceptedEvent) {
      handleAudioAcceptedEventCalled = true;
    }

    @Override
    public void handle(AudioDownloadedEvent audioDownloadedEvent) {
      handleAudioDownloadedEventCalled = true;
    }

    @Override
    public void handle(AudioDownloadProgressUpdatedEvent progressUpdatedEvent) {
      handleProgressUpdatedEventCalled = true;
    }

    @Override
    public void handle(UnableDownloadAudioEvent unableDownloadAudioEvent) {
      handleUnableDownloadAudioEventCalled = true;
    }
  };
  private boolean handleAudioAcceptedEventCalled;
  private boolean handleAudioDownloadedEventCalled;
  private boolean handleProgressUpdatedEventCalled;
  private boolean handleUnableDownloadAudioEventCalled;

  @Before
  public void setUp() throws Exception {
    handleAudioAcceptedEventCalled = false;
    handleAudioDownloadedEventCalled = false;
    handleProgressUpdatedEventCalled = false;
    handleUnableDownloadAudioEventCalled = false;
  }

  @Test
  public void startHandlingCalledTwice_iseThrown() {
    try {
      eventsHandler.startHandling(Schedulers.immediate());
      eventsHandler.startHandling(Schedulers.immediate());
      fail("Ise was not thrown");
    } catch (IllegalStateException expected) {
      assertThat(expected.getMessage())
              .isEqualTo("Already handling");
    }
  }

  @Test
  public void stopHandlingCalledWithoutStartHandlingCall_iseThrown() {
    try {
      eventsHandler.startHandling(Schedulers.immediate());
      eventsHandler.startHandling(Schedulers.immediate());
      fail("Ise was not thrown");
    } catch (IllegalStateException expected) {
      assertThat(expected.getMessage())
              .isEqualTo("Already handling");
    }
  }

  @Test
  public void audioAcceptedEventPosted_handleAudioAcceptedEventCalled() {
    eventsHandler.startHandling(Schedulers.immediate());
    rxBus.post(new AudioAcceptedEvent(new Audio()));
    assertThat(handleAudioAcceptedEventCalled)
            .isTrue();
  }

  @Test
  public void audioDownloadedEventPosted_handleAudioDownloadedEventCalled() {
    eventsHandler.startHandling(Schedulers.immediate());
    rxBus.post(new AudioDownloadedEvent(new Audio()));
    assertThat(handleAudioDownloadedEventCalled)
            .isTrue();
  }

  @Test
  public void audioDownloadProgressUpdatedEventPosted_handleAudioDownloadProgressUpdatedCalled() {
    eventsHandler.startHandling(Schedulers.immediate());
    rxBus.post(AudioDownloadProgressUpdatedEvent.builder().audio(new Audio()).build());
    assertThat(handleProgressUpdatedEventCalled)
            .isTrue();
  }

  @Test
  public void unableDownloadAudioEventPosted_handleUnableDownloadAudioCalled() {
    eventsHandler.startHandling(Schedulers.immediate());
    rxBus.post(new UnableDownloadAudioEvent(new IOException(), new Audio()));
    assertThat(handleUnableDownloadAudioEventCalled)
            .isTrue();
  }

  @Test
  public void startHandlingWasNotCalled_noEventsHandled() {
    rxBus.post(new UnableDownloadAudioEvent(new IOException(), new Audio()));
    rxBus.post(new AudioAcceptedEvent(new Audio()));
    rxBus.post(AudioDownloadProgressUpdatedEvent.builder().audio(new Audio()).build());
    rxBus.post(new AudioDownloadedEvent(new Audio()));

    assertThat(handleUnableDownloadAudioEventCalled)
            .isFalse();
    assertThat(handleProgressUpdatedEventCalled)
            .isFalse();
    assertThat(handleAudioDownloadedEventCalled)
            .isFalse();
    assertThat(handleAudioAcceptedEventCalled)
            .isFalse();
  }

  @Test
  public void stopHandlingCalledAfterStartHandlingCall_noEventsHandled() {
    eventsHandler.startHandling(Schedulers.immediate());
    eventsHandler.stopHandling();

    rxBus.post(new UnableDownloadAudioEvent(new IOException(), new Audio()));
    rxBus.post(new AudioAcceptedEvent(new Audio()));
    rxBus.post(AudioDownloadProgressUpdatedEvent.builder().audio(new Audio()).build());
    rxBus.post(new AudioDownloadedEvent(new Audio()));

    assertThat(handleUnableDownloadAudioEventCalled)
            .isFalse();
    assertThat(handleProgressUpdatedEventCalled)
            .isFalse();
    assertThat(handleAudioDownloadedEventCalled)
            .isFalse();
    assertThat(handleAudioAcceptedEventCalled)
            .isFalse();
  }
}
