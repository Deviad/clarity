package com.clarity.transactiondispatcher.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class InfuraSocketConfiguration {

    @Autowired
    private Web3jService web3jService;

//    @Bean("web3jSocketConnection")
//    @SneakyThrows
//    Web3j webSocketSubscription() {
//        final WebSocketClient webSocketClient = new WebSocketClient(new URI(web3jService.getInfuraWsEndpoint()));
//        final boolean includeRawResponses = false;
//        final WebSocketService webSocketService = new WebSocketService(webSocketClient, includeRawResponses);
//        webSocketClient.connect();
//        return Web3j.build(webSocketService);
//    }

    //    private Consumer<String> logger() {
//        return (String s) -> log.info("Logging web3j subscription: " + s);
//    }
//    @SneakyThrows
//    @Bean("webSocketSubscription")
//    Mono<Void> wsConnectNetty() {
//        URI uri = new URI(web3jService.getInfuraWsEndpoint());
//      return new ReactorNettyWebSocketClient().execute(uri,
//                session -> session
//                        .receive()
//                        .map(WebSocketMessage::getPayloadAsText)
////                        .take(MAX_EVENTS)
//                        .doOnNext(txt -> log.info(session.getId() + ".IN: " + txt))
//                        .flatMap(txt -> session.send(Mono.just(session.textMessage(txt))))
//                        .doOnSubscribe(subscriber -> log.info(session.getId() + ".OPEN"))
//                        .doFinally(signalType -> {session.close(); log.info(session.getId() + ".CLOSE"); })
//                        .then()
//
//        );
//    }
    @SneakyThrows
    @Bean("webSocketSubscription")
    void wsConnectNetty() {
        Map<String, Object> config = Stream.of(
                new AbstractMap.SimpleEntry<>("includeTransactions", true)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Object> map = Stream.of(
                new AbstractMap.SimpleEntry<>("id", 1),
                new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
                new AbstractMap.SimpleEntry<>("params", new Object[]{"newHeads", config})
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(map);

        Flux<String> input = Flux.generate(sink -> sink.next(json));

        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();
        EmitterProcessor<String> output = EmitterProcessor.create();

        Mono<Void> sessionMono = client.execute(URI.create(web3jService.getInfuraWsEndpoint()), session -> session.send(input.map(session::textMessage))
                .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText).subscribeWith(output).then()).then());

        output.doOnSubscribe(s -> {
            log.info(s.toString());
            sessionMono.subscribe();
        });
    }

}
