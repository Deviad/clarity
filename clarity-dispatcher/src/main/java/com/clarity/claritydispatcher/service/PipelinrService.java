package com.clarity.claritydispatcher.service;

import an.awesome.pipelinr.Pipeline;
import io.micronaut.runtime.http.scope.RequestScope;
import lombok.Data;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@RequestScope
@Data
public class PipelinrService {
  private final Optional<QueryPipeline> queryPipeline;
  private final Optional<CommandPipeline> commandPipeline;

  @Inject
  public PipelinrService(Optional<QueryPipeline> queryPipeline, Optional<CommandPipeline> commandPipeline) {
    this.queryPipeline = queryPipeline;
    this.commandPipeline = commandPipeline;
  }
}
