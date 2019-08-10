package com.clarity.claritydispatcher.service;

import an.awesome.pipelinr.Pipeline;
import lombok.Data;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Data
public class PipelinrService {
  private final Pipeline queryPipeline;

  private final Pipeline commandPipeline;

  @Inject
  public PipelinrService(
      @QueryPipeline Pipeline queryPipeline, @CommandPipeline Pipeline commandPipeline) {
    this.queryPipeline = queryPipeline;
    this.commandPipeline = commandPipeline;
  }
}
