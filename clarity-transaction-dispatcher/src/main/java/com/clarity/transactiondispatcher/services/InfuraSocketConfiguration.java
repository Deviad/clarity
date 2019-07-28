package com.clarity.transactiondispatcher.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;
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
import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;
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

        return clientFactory.apply(map);

    }

    @Bean
    public ApplicationRunner applicationRunner(final @Qualifier("webSocketSubscription") Flux<String> ws) {
        return applicationArguments -> ws.subscribe(log::info);
    }


    @Bean
    ReactorNettyWebSocketClient client() {
        return new ReactorNettyWebSocketClient();
    }

    @Bean
    EmitterProcessor<String> emitterProcessor() {
        return EmitterProcessor.create();
    }

    public Function<Map<String, Object>, Flux<String>> clientFactory = (map)->{

//        ReactorNettyWebSocketClient client = nettyInstance();
//        EmitterProcessor<String> output = EmitterProcessor.create();

        ObjectMapper objectMapper = new ObjectMapper();

        String json = Unchecked.supplier(()->objectMapper.writeValueAsString(map)).get();

        Flux<String> input = Flux.<String>generate(sink -> sink.next(json))
                .delayElements(Duration.ofSeconds(1));

        Mono<Void> sessionMono = client().execute(URI.create(web3jService.getInfuraWsEndpoint()), session -> session.send(input.map(session::textMessage))
                .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText).subscribeWith(emitterProcessor()).then()).then());

        return emitterProcessor().doOnSubscribe(s -> sessionMono.subscribe()).doOnError(x -> log.info(x.getMessage()));

    };

}
