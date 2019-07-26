package com.clarity.transactiondispatcher.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class InfuraSocketConfiguration {

    @Autowired
    private Web3jService web3jService;

    @SneakyThrows
    @Bean("webSocketSubscription")
    Flux<String> wsConnectNetty() {
        Map<String, Object> map = Stream.of(
                new AbstractMap.SimpleEntry<>("id", 1),
                new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
                new AbstractMap.SimpleEntry<>("params", new Object[]{"newHeads"})
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(map);

        Flux<String> input = Flux.generate(sink -> sink.next(json));

        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();
        EmitterProcessor<String> output = EmitterProcessor.create();

        Mono<Void> sessionMono = client.execute(URI.create(web3jService.getInfuraWsEndpoint()), session -> session.send(input.map(session::textMessage))
                .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText).subscribeWith(output).then()).then());

        return output.doOnSubscribe(s -> {
            sessionMono.subscribe();
        }).doOnError(x -> log.info(x.getMessage()));
    }

    @Bean
    public ApplicationRunner applicationRunner(final @Qualifier("webSocketSubscription") Flux<String> ws) {
        return applicationArguments -> ws.subscribe(log::info);
    }

}
