package com.clarity.claritydispatcher.service;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.PipelineStep;

public class PipelineStepImpl implements PipelineStep {

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        return next.invoke();
    }
}
