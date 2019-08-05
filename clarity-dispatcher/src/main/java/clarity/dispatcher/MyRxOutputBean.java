package clarity.dispatcher;


import reactor.core.publisher.EmitterProcessor;

class MyRxOutputBean<T> {

    private EmitterProcessor<T> subject = EmitterProcessor.create();

    /** Pass any event down to event listeners. */
    public void setObject(T object) {
        subject.onNext(object);
    }

    /** Subscribe to this Observable. On event, do something e.g. replace a fragment */
    public EmitterProcessor<T> getEvents() {
        return subject;
    }
}
