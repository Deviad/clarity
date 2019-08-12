package com.clarity.claritydispatcher;

import com.clarity.claritydispatcher.service.PipelineStepImpl;
import com.clarity.claritydispatcher.service.QueryPipeline;
import com.clarity.claritydispatcher.web.handler.EthereumAccountCreate;
import com.clarity.claritydispatcher.web.handler.EthereumAccountGetBalance;
import com.clarity.claritydispatcher.web.handler.EthereumTransactionCreate;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;
import java.util.stream.Stream;

@Factory
public class PipelinrConfiguration {
  @Singleton
  @Bean
  QueryPipeline pipeline() {
    return new QueryPipeline(
        () ->
            Stream.of(
                new EthereumAccountCreate.Handler(),
                new EthereumAccountGetBalance.Handler(),
                new EthereumTransactionCreate.Handler()),
        () -> Stream.of(new PipelineStepImpl()));
  };
}
