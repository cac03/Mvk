package com.caco3.mvk.rxbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class RxBus {
  private final Subject<Object, Object> subject = new SerializedSubject<>(PublishSubject.create());
  private static volatile RxBus instance;

  public static RxBus getInstance() {
    if (instance == null) {
      synchronized (RxBus.class) {
        if (instance == null) {
          instance = new RxBus();
        }
      }
    }

    return instance;
  }

  private RxBus(){
  }

  public void post(Object event) {
    checkNotNull(event, "event == null");
    subject.onNext(event);
  }

  public Observable<Object> observable() {
    return subject;
  }
}
