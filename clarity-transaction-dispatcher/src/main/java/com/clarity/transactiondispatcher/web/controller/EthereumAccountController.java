package com.clarity.transactiondispatcher.web.controller;


import com.clarity.transactiondispatcher.services.EthereumOperations;
import com.clarity.transactiondispatcher.services.PipelinrService;
import com.clarity.transactiondispatcher.web.handler.EthereumAccountCreate;
import com.clarity.transactiondispatcher.web.handler.EthereumAccountGetBalance;
import com.clarity.transactiondispatcher.web.model.AccountBalanceRequestDTO;
import com.clarity.transactiondispatcher.web.model.AccountRequestDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
public class EthereumAccountController {


    private final PipelinrService pipelinrService;
    private final EthereumOperations operations;

    public EthereumAccountController(PipelinrService pipelinrService, EthereumOperations operations) {
        this.pipelinrService = pipelinrService;
        this.operations = operations;
    }

    @PostMapping("/ethaccount")
    @SneakyThrows
    public Mono<Map<String, Object>> createAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        return pipelinrService.getQueryPipeline().send(new EthereumAccountCreate(accountRequestDTO, operations));
    }

    @PostMapping("/ethaccount/balance")
    @SneakyThrows
    public Mono<Map<String, Object>> getAccountBalance(@RequestBody @Valid AccountBalanceRequestDTO accountBalanceRequestDTO) {
        return pipelinrService.getQueryPipeline().send(new EthereumAccountGetBalance(accountBalanceRequestDTO, operations));
    }

    @PostMapping("/ethaccount/updatedbalance")
    @SneakyThrows
    public Mono<Map<String, Object>> getUpdatedAccountBalance(@RequestBody @Valid AccountBalanceRequestDTO accountBalanceRequestDTO) {
        return pipelinrService.getQueryPipeline().send(new EthereumAccountGetBalance(accountBalanceRequestDTO, operations));
    }
}
