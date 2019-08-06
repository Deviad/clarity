package clarity.dispatcher;

import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class MyRxBusConfig {

  @Singleton
  MyRxInputBean<String> inputMyRxBean() {
    return new MyRxInputBean<>();
  }

  @Singleton
  MyRxOutputBean<Output> outputMyRxBean() {
    return new MyRxOutputBean<>();
  }
}
