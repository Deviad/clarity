package clarity.dispatcher;

import io.micronaut.context.annotation.Bean;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import javax.inject.Singleton;

@Singleton
public class MyRxBus<T> {

    private PublishSubject<T> subject = PublishSubject.create();

    /**
     * Pass any event down to event listeners.
     */
    public void setObject(T object) {
        subject.onNext(object);
    }

    /**
     * Subscribe to this Observable. On event, do something
     * e.g. replace a fragment
     */
    public Observable<T> getEvents() {
        return subject;
    }
}
