package com.clarity.transactiondispatcher.web.controller;

import an.awesome.pipelinr.Pipeline;
import com.clarity.transactiondispatcher.web.handler.EthereumTransactionReadAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@Slf4j
public class EthereumTransactionController {

    private final Pipeline queryPipeline;

    private final Pipeline commandPipeline;

    @Value("${infura.endpoint}")
    private String infuraEndpoint;

    public EthereumTransactionController(@Qualifier("queryPipelinr") Pipeline queryPipeline, @Qualifier("commandPipelinr") Pipeline commandPipeline) {
        this.queryPipeline = queryPipeline;
        this.commandPipeline = commandPipeline;
    }


    public Mono<ServerResponse> createTransaction(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just("data"), String.class));
    }

    public Mono<ServerResponse> readTransaction(ServerRequest request) {

        Web3j web3 = Web3j.build(new HttpService(infuraEndpoint));  // defaults to http://localhost:8545/





        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just(infuraEndpoint), String.class));
    }

//    public Mono<ServerResponse> readAll(ServerRequest request) {
//        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromObject(new ArrayList<>(Arrays.asList("1", "2"))));
//    }

    public Mono<ServerResponse> readAll(ServerRequest request) {

        CompletableFuture<String> json = queryPipeline.send(new EthereumTransactionReadAll(new ArrayList<>(Arrays.asList("1", "2"))));
        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just(json.getNow("")), String.class));
    }

}
