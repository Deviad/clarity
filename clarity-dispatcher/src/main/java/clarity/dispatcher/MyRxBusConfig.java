package clarity.dispatcher;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.runtime.http.scope.RequestScope;

import javax.inject.Named;

@Context
public class MyRxBusConfig {

  @Bean
  @RequestScope
  @Named("INPUT")
  MyRxBean<String> inputMyRxBean() {
    return new MyRxBean<>();
  }

  @Bean
  @RequestScope
  @Named("OUTPUT")
  MyRxBean<String> outputMyRxBean() {
    return new MyRxBean<>();
  }
}
