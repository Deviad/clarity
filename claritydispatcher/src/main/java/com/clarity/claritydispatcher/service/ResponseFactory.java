package com.clarity.claritydispatcher.service;

import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ResponseFactory {
  default Mono<Map<String, Object>> getSuccessMonoResponse(Object object) {
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("status", "success");
    result.put("data", object);
    return Mono.just(result);
  }

  default Mono<Map<String, Object>> getJsonErrMonoResp(Object object) {
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("status", "error");
    result.put("message", object);

    return Mono.just(result);
  }

  default Map<String, Object> getSuccessScalarResponse(Object object) {
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("status", "success");
    result.put("data", object);
    return result;
  }

  default Map<String, Object> getJsonErrScalarResp(Throwable throwable) {
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("status", "error");
    result.put("message", throwable.getMessage());
    result.put("stacktrace", throwable.getStackTrace());
    return result;
  }
}
