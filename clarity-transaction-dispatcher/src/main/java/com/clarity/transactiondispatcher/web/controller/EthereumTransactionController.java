package com.clarity.transactiondispatcher.web.controller;

import com.clarity.transactiondispatcher.services.EthereumOperations;
import com.clarity.transactiondispatcher.services.PipelinrService;
import com.clarity.transactiondispatcher.web.handler.EthereumTransactionCreate;
import com.clarity.transactiondispatcher.web.model.TransactionRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

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
    public Mono<Map<String, Object>> createTransaction(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
      return pipelinrService.getQueryPipeline()
              .send(new EthereumTransactionCreate(transactionRequestDTO, ethereumOperations));
    }

//    public Mono<ServerResponse> readTransaction(ServerRequest request) {
//
//        Web3j web3 = Web3j.build(new HttpService(infuraEndpoint));  // defaults to http://localhost:8545/
//
//
//
//
//
//        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just(infuraEndpoint), String.class));
//    }
//
//    public Mono<ServerResponse> readAll(ServerRequest request) {
//
//        CompletableFuture<String> json = queryPipeline.send(new EthereumTransactionReadAll(new ArrayList<>(Arrays.asList("1", "2"))));
//        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just(json.getNow("")), String.class));
//    }

}
