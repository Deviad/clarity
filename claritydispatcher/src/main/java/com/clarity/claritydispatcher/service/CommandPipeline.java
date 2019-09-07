package com.clarity.claritydispatcher.service;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.PipelineStep;
import an.awesome.pipelinr.Pipelinr;
import an.awesome.pipelinr.StreamSupplier;

public class CommandPipeline extends Pipelinr {

  public CommandPipeline(StreamSupplier<Command.Handler> commandHandlers) {
    super(commandHandlers);
  }

  public CommandPipeline(
      StreamSupplier<Command.Handler> commandHandlers, StreamSupplier<PipelineStep> steps) {
    super(commandHandlers, steps);
  }
}
