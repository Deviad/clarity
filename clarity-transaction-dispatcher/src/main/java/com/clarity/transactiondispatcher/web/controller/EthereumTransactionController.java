package com.clarity.transactiondispatcher.web.controller;

import java.util.Map;

import javax.validation.Valid;

import com.clarity.transactiondispatcher.services.EthereumOperations;
import com.clarity.transactiondispatcher.services.PipelinrService;
import com.clarity.transactiondispatcher.web.handler.EthereumTransactionCreate;
import com.clarity.transactiondispatcher.web.model.TransactionRequestDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class EthereumTransactionController {

    private final PipelinrService pipelinrService;
    private final EthereumOperations ethereumOperations;

    public EthereumTransactionController(PipelinrService pipelinrService, EthereumOperations ethereumOperations) {
        this.pipelinrService = pipelinrService;
        this.ethereumOperations = ethereumOperations;
    }

    @PostMapping("/ethtransaction")
    public Mono<Map<String, Object>> createTransaction(
            @RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
        return pipelinrService.getQueryPipeline()
                .send(new EthereumTransactionCreate(transactionRequestDTO, ethereumOperations));
    }

}
