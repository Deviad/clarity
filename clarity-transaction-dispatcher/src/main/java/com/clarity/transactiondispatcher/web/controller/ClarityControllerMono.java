package com.clarity.transactiondispatcher.web.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.lambda.Unchecked;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public interface ClarityControllerMono {



    default Mono<ServerResponse> getJsonSuccessResp (Object object) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "success");
        result.put("data", object);

        return ok()
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(toJSON(result)), String.class));
    }


    default Mono<ServerResponse> getJsonErrsResp (Object object) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "error");
        result.put("message", object);

        return ok()
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(toJSON(result)), String.class));
    }

    private String toJSON(Object object) {

            ObjectMapper objectMapper = new ObjectMapper();
            return Unchecked.function(objectMapper::writeValueAsString).apply(object);
        }

}
