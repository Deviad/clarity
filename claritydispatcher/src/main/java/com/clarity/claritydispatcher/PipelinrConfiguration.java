package com.clarity.claritydispatcher;

import an.awesome.pipelinr.Command;
import com.clarity.claritydispatcher.service.CommandPipeline;
import com.clarity.claritydispatcher.service.PipelineStepImpl;
import com.clarity.claritydispatcher.service.QueryPipeline;
import com.clarity.claritydispatcher.web.handler.EthereumAccountCreate;
import com.clarity.claritydispatcher.web.handler.EthereumAccountGetBalance;
import com.clarity.claritydispatcher.web.handler.EthereumTransactionCreate;
import com.clarity.claritydispatcher.web.handler.HyperledgerAccountHandler;
import com.clarity.clarityshared.Query;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;
import java.util.stream.Stream;

@Factory
public class PipelinrConfiguration {
    @Singleton
    @Bean
    QueryPipeline queryPipeline() {
        return new QueryPipeline(
                () -> Stream.of(ethereumQueryHandlers()),
                () -> Stream.of(new PipelineStepImpl()));
    }

    @Singleton
    @Bean
    CommandPipeline commandPipeline() {
        return new CommandPipeline(
                () -> Stream.of(ethereumCommandHandlers()),
                () -> Stream.of(new PipelineStepImpl()));
    }

    private Command.Handler[] ethereumCommandHandlers() {
        return new Command.Handler[]{
                new EthereumAccountCreate.Handler(),
                new EthereumTransactionCreate.Handler(),
                new HyperledgerAccountHandler.Handler()
        };
    }

    private Query.Handler[] ethereumQueryHandlers() {
        return new Query.Handler[]{new EthereumAccountGetBalance.Handler()};
    }
}
