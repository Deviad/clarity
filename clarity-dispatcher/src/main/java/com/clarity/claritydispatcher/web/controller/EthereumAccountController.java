package com.clarity.claritydispatcher.web.controller;

import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.PipelinrService;
import com.clarity.claritydispatcher.web.handler.EthereumAccountCreate;
import com.clarity.claritydispatcher.web.handler.EthereumAccountGetBalance;
import com.clarity.claritydispatcher.web.model.AccountBalanceRequestDTO;
import com.clarity.claritydispatcher.web.model.AccountRequestDTO;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import java.util.Map;

@Controller(value = "/ethaccount")
public class EthereumAccountController {

  private final PipelinrService pipelinrService;
  private final EthereumService ethService;

  @Inject
  public EthereumAccountController(PipelinrService pipelinrService, EthereumService ethService) {
    this.pipelinrService = pipelinrService;
    this.ethService = ethService;
  }

  @Post(value = "/function/create")
  public Mono<Map<String, Object>> createAccount(@Valid AccountRequestDTO accountRequestDTO) {
    return pipelinrService
        .getQueryPipeline().orElseThrow()
        .send(new EthereumAccountCreate(accountRequestDTO, ethService));
  }

  @Post(value = "/function/getbalance")
  public Mono<Map<String, Object>> getAccountBalance(
      @Valid AccountBalanceRequestDTO accountBalanceRequestDTO) {
    return pipelinrService
        .getQueryPipeline().orElseThrow()
        .send(new EthereumAccountGetBalance(accountBalanceRequestDTO, ethService));
  }
}
