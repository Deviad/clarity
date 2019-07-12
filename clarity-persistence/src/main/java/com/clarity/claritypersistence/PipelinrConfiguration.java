package com.clarity.claritypersistence;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.PipelineStep;
import an.awesome.pipelinr.Pipelinr;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelinrConfiguration {

    @Bean("commandPipelinr")
    Pipeline commandPipeline(ObjectProvider<Cmd.Handler> commandHandlers, ObjectProvider<PipelineStep> pipelineSteps) {
        return new Pipelinr(commandHandlers::stream, pipelineSteps::orderedStream);
    }

    @Bean("queryPipelinr")
    Pipeline queryPipeline(ObjectProvider<Query.Handler> commandHandlers, ObjectProvider<PipelineStep> pipelineSteps) {
        return new Pipelinr(commandHandlers::stream, pipelineSteps::orderedStream);
    }
}


interface Cmd<R> extends Command<R> {
}

interface Query<R> extends Command<R> {
}
