package com.clarity.claritydispatcher.web.controller;


import an.awesome.pipelinr.Pipeline;
import com.clarity.claritydispatcher.service.CommandPipeline;
import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.PipelinrService;
import com.clarity.claritydispatcher.service.QueryPipeline;
import com.clarity.claritydispatcher.web.handler.EthereumTransactionCreate;
import com.clarity.claritydispatcher.web.model.TransactionRequestDTO;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller("/ethtransaction")
@Slf4j
public class EthereumTransactionController {

    private final PipelinrService pipelinrService;
    private final EthereumService ethereumService;

    public EthereumTransactionController(PipelinrService pipelinrService, EthereumService ethereumService) {
        this.pipelinrService = pipelinrService;
        this.ethereumService = ethereumService;
    }

    @Post("/create")
    public Mono<Map<String, Object>> createTransaction(
           @Valid TransactionRequestDTO transactionRequestDTO) {
        return pipelinrService.getQueryPipeline()
                .send(new EthereumTransactionCreate(transactionRequestDTO, ethereumService));
    }
}
