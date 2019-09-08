package com.clarity.claritydispatcher;

import io.reactivex.subjects.BehaviorSubject;
import reactor.core.publisher.EmitterProcessor;

public class MyRxOutputBean<T> {

  private BehaviorSubject<T> subject = BehaviorSubject.create();

  /** Pass any event down to event listeners. */
  public void setObject(T object) {
    subject.onNext(object);
  }

  /** Subscribe to this Observable. On event, do something e.g. replace a fragment */
  public BehaviorSubject<T> getEvents() {
    return subject;
  }
}
