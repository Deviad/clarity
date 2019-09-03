package com.clarity.claritydispatcher.web.controller;

import com.clarity.claritydispatcher.EthereumWebsocketConnectionFacade;
import com.clarity.claritydispatcher.Output;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.http.scope.RequestScope;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller("/sse/ethaccount")
@RequestScope
@Slf4j
public class EthereumAccountControllerSSE {
  private EthereumWebsocketConnectionFacade cf;

  @PostConstruct
  void init() {
    cf = new EthereumWebsocketConnectionFacade();
  }

  @Get(value = "/getnewheads", produces = MediaType.TEXT_EVENT_STREAM)
  @SneakyThrows
  Flux<String> getBalance() {

    Map<String, Object> map =
        Stream.of(
                new AbstractMap.SimpleEntry<>("id", 1),
                new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
                new AbstractMap.SimpleEntry<>("params", new Object[] {"newHeads"}))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return connectionFactory(map);
  }

  @SneakyThrows
  private Flux<String> connectionFactory(Map<String, Object> map) {
    final ObjectMapper mapper = new ObjectMapper();
    String json = Unchecked.supplier(() -> mapper.writeValueAsString(map)).get();

    return Flux.interval(Duration.ofMillis(500))
        .doOnNext(x -> cf.connect(json))
        .subscribeOn(Schedulers.single())
        .flatMap(x -> cf.getOutputBus().getEvents().replay(1).autoConnect().map(Output::getText))
        .distinct()
        .doFinally(
            signalType ->
                cf.getOutputBus().getEvents().doOnNext(x -> x.getWebSocket().cancel()).subscribe());
  }
}
