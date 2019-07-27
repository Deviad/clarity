package clarity.dispatcher;

import io.micronaut.discovery.event.ServiceStartedEvent;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;

import javax.inject.Singleton;


public class Application {

    public static void main(String[] args) {

        Micronaut.run(Application.class);
    }
}

