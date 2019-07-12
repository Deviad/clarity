package com.clarity.transactiondispatcher.web.controller;

import an.awesome.pipelinr.Pipeline;
import com.clarity.transactiondispatcher.web.handler.EthereumReadAll;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class EthereumController {

    private final Pipeline queryPipeline;

    private final Pipeline commandPipeline;

    public EthereumController(@Qualifier("queryPipelinr") Pipeline queryPipeline, @Qualifier("commandPipelinr") Pipeline commandPipeline) {
        this.queryPipeline = queryPipeline;
        this.commandPipeline = commandPipeline;
    }


    public Mono<ServerResponse> createTransaction(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just("data"), String.class));
    }

    public Mono<ServerResponse> readTransaction(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just("data"), String.class));
    }

//    public Mono<ServerResponse> readAll(ServerRequest request) {
//        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromObject(new ArrayList<>(Arrays.asList("1", "2"))));
//    }

    public Mono<ServerResponse> readAll(ServerRequest request) {
        CompletableFuture<String> json = queryPipeline.send(new EthereumReadAll(new ArrayList<>(Arrays.asList("1", "2"))));
        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just(json.getNow("")), String.class));
    }

}
