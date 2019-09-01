package com.clarity.claritydispatcher.web.controller;

import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.KafkaProducerService;
import com.clarity.claritydispatcher.service.PipelinrService;
import com.clarity.claritydispatcher.web.handler.EthereumAccountCreate;
import com.clarity.claritydispatcher.web.handler.EthereumAccountGetBalance;
import com.clarity.claritydispatcher.web.model.AccountBalanceRequestDTO;
import com.clarity.claritydispatcher.web.model.AccountRequestDTO;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Map;

@Controller(value = "/ethaccount")
public class EthereumAccountController {

  private final PipelinrService pipelinrService;
  private final EthereumService ethService;
  private final KafkaProducerService kafkaProducer;
  @Inject
  public EthereumAccountController(PipelinrService pipelinrService, EthereumService ethService, KafkaProducerService kafkaProducer) {
    this.pipelinrService = pipelinrService;
    this.ethService = ethService;
    this.kafkaProducer = kafkaProducer;
  }

  @Post(value = "/function/create")
  public Mono<Map<String, Object>> createAccount(@Valid AccountRequestDTO accountRequestDTO) {
    return pipelinrService
        .getQueryPipeline().orElseThrow()
        .send(new EthereumAccountCreate(accountRequestDTO, ethService, kafkaProducer));
  }

  @Post(value = "/function/getbalance")
  public Mono<Map<String, Object>> getAccountBalance(
      @Valid AccountBalanceRequestDTO accountBalanceRequestDTO) {
    return pipelinrService
        .getQueryPipeline().orElseThrow()
        .send(new EthereumAccountGetBalance(accountBalanceRequestDTO, ethService));
  }
}
