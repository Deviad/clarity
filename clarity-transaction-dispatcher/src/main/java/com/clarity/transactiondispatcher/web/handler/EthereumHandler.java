package com.clarity.transactiondispatcher.web.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class EthereumHandler {

    public Mono<ServerResponse> createTransaction(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just("data"), String.class));
    }

    public Mono<ServerResponse> readTransaction(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just("data"), String.class));
    }

    public Mono<ServerResponse> readAll(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromObject(new ArrayList<>(Arrays.asList("1", "2"))));
    }
}
