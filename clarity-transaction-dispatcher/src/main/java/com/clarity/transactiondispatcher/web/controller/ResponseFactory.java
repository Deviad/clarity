package com.clarity.transactiondispatcher.web.controller;


import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ResponseFactory {
    default Mono<Map<String, Object>> getSuccessResponse(Object object) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "success");
        result.put("data", object);
        return Mono.just(result);
    }
}
