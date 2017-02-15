package com.caco3.mvk.audiodownload.events.handle;

import com.caco3.mvk.audiodownload.events.AudioAcceptedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadProgressUpdatedEvent;
import com.caco3.mvk.audiodownload.events.AudioDownloadedEvent;
import com.caco3.mvk.audiodownload.events.UnableDownloadAudioEvent;
import com.caco3.mvk.rxbus.RxBus;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;

import static com.caco3.mvk.util.Preconditions.checkNotNull;
import static com.caco3.mvk.util.Preconditions.checkState;

public abstract class AbstractAudioDownloadEventsHandler implements AudioDownloadEventsHandler {
  private final RxBus rxBus;
  private final Action1<? super Object> handleEventAction = new Action1<Object>() {
    @Override
    public void call(Object o) {
      if (o instanceof AudioAcceptedEvent) {
        handle((AudioAcceptedEvent)o);
      } else if (o instanceof AudioDownloadedEvent) {
        handle((AudioDownloadedEvent)o);
      } else if (o instanceof AudioDownloadProgressUpdatedEvent) {
        handle((AudioDownloadProgressUpdatedEvent)o);
      } else if (o instanceof UnableDownloadAudioEvent) {
        handle((UnableDownloadAudioEvent)o);
      }
    }
  };
  private Subscription subscription;

  protected AbstractAudioDownloadEventsHandler(RxBus rxBus) {
    this.rxBus = checkNotNull(rxBus, "rxBus == null");
  }

  public void startHandling(Scheduler scheduler) {
    checkState(subscription == null, "Already handling");
    subscription = rxBus.observable()
            .observeOn(scheduler)
            // TODO: 2/15/17 throttleLast(...) ?
            .subscribe(handleEventAction);
  }

  public void stopHandling() {
    checkState(subscription != null, "Was not handling");
    subscription.unsubscribe();
    subscription = null;
  }
}
