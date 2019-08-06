package clarity.dispatcher;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import io.micronaut.runtime.http.scope.RequestScope;
import jnr.ffi.annotations.Out;

import javax.inject.Named;
import javax.inject.Qualifier;
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
