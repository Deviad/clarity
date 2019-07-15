package com.clarity.transactiondispatcher.services;

import an.awesome.pipelinr.Pipeline;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PipelinrService {
    private final Pipeline queryPipeline;

    private final Pipeline commandPipeline;

    public PipelinrService(@Qualifier("queryPipelinr") Pipeline queryPipeline,
            @Qualifier("commandPipelinr") Pipeline commandPipeline) {
        this.queryPipeline = queryPipeline;
        this.commandPipeline = commandPipeline;
    }
}
