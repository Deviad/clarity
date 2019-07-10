package com.clarity.transactiondispatcher.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class EthereumHandler {

    public Mono<ServerResponse> createTransaction(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(null);
    }

    public Mono<ServerResponse> readTransaction(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(null);
    }

    public Mono<ServerResponse> readAll(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(null);
    }

}
