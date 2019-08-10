package com.clarity.claritydispatcher.web.controller;

import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.PipelinrService;
import com.clarity.claritydispatcher.web.handler.EthereumAccountCreate;
import com.clarity.claritydispatcher.web.handler.EthereumAccountGetBalance;
import com.clarity.claritydispatcher.web.model.AccountBalanceRequestDTO;
import com.clarity.claritydispatcher.web.handler.EthereumAccountCreate;
import com.clarity.web.model.AccountRequestDTO;
import io.micronaut.http.annotation.Post;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Map;

public class EthereumAccountController {

  private final PipelinrService pipelinrService;
  private final EthereumService ethService;

  @Inject
  public EthereumAccountController(PipelinrService pipelinrService, EthereumService ethService) {
    this.pipelinrService = pipelinrService;
    this.ethService = ethService;
  }

  @Post("/ethaccount")
  @SneakyThrows
  public Mono<Map<String, Object>> createAccount(@Valid AccountRequestDTO accountRequestDTO) {
    return pipelinrService
        .getQueryPipeline()
        .send(new EthereumAccountCreate(accountRequestDTO, ethService));
  }

  @Post("/ethaccount/balance")
  @SneakyThrows
  public Mono<Map<String, Object>> getAccountBalance(
      @Valid AccountBalanceRequestDTO accountBalanceRequestDTO) {
    return pipelinrService
        .getQueryPipeline()
        .send(new EthereumAccountGetBalance(accountBalanceRequestDTO, ethService));
  }
}
