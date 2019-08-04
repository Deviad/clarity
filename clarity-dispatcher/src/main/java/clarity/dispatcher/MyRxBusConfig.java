package clarity.dispatcher;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class MyRxBusConfig {

  @Bean
  @Singleton
  @Named("INPUT")
  MyRxBean<String> inputMyRxBean() {
    return new MyRxBean<>();
  }

  @Bean
  @Singleton
  @Named("OUTPUT")
  MyRxBean<String> outputMyRxBean() {
    return new MyRxBean<>();
  }
}
