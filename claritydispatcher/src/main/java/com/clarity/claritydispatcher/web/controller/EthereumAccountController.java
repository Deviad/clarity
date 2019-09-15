package com.clarity.claritydispatcher.web.controller;

import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.KafkaMessageListener;
import com.clarity.claritydispatcher.service.KafkaService;
import com.clarity.claritydispatcher.service.PipelinrService;
import com.clarity.claritydispatcher.web.handler.EthereumAccountCreate;
import com.clarity.claritydispatcher.web.handler.EthereumAccountGetBalance;
import com.clarity.claritydispatcher.web.model.AccountBalanceRequestDTO;
import com.clarity.claritydispatcher.web.model.AccountRequestDTO;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import net.jodah.failsafe.Failsafe;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Map;

@Controller(value = "/ethaccount")
public class EthereumAccountController {

  private final PipelinrService pipelinrService;
  private final EthereumService ethService;
  private final KafkaService kafkaservice;
  private final KafkaMessageListener kafkaListener;

  @Inject
  public EthereumAccountController(PipelinrService pipelinrService, EthereumService ethService, KafkaService kafkaservice, KafkaMessageListener kafkaListener) {
    this.pipelinrService = pipelinrService;
    this.ethService = ethService;
    this.kafkaservice = kafkaservice;
    this.kafkaListener = kafkaListener;
  }

  @Post(value = "/function/create")
  public Mono<Map<String, Object>> createAccount(@Valid AccountRequestDTO accountRequestDTO) {
    return pipelinrService
        .getCommandPipeline().orElseThrow()
        .send(new EthereumAccountCreate(accountRequestDTO, ethService, kafkaservice, kafkaListener));
  }

  @Post(value = "/function/getbalance")
  public Mono<Map<String, Object>> getAccountBalance(
      @Valid AccountBalanceRequestDTO accountBalanceRequestDTO) {
    return pipelinrService
        .getQueryPipeline().orElseThrow()
        .send(new EthereumAccountGetBalance(accountBalanceRequestDTO, ethService));
  }
}