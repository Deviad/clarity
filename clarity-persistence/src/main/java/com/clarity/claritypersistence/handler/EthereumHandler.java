package com.clarity.claritypersistence.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.DataInput;
import java.util.*;

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

    @SneakyThrows
    public Mono<ServerResponse> readAll(ServerRequest request) {

        Map<String, Object> test = new LinkedHashMap<>();

        test.put("contents", Arrays.asList("1", "2"));

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(test);

        return ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(Mono.just(json), String.class));
    }

}
