package com.clarity.claritydispatcher.service;


import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.PipelineStep;
import an.awesome.pipelinr.Pipelinr;
import an.awesome.pipelinr.StreamSupplier;

public class QueryPipeline extends Pipelinr {

    public QueryPipeline(StreamSupplier<Command.Handler> commandHandlers) {
        super(commandHandlers);
    }

    public QueryPipeline(
            StreamSupplier<Command.Handler> commandHandlers, StreamSupplier<PipelineStep> steps) {
        super(commandHandlers, steps);
    }
}
