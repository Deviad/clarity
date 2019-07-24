package com.clarity.transactiondispatcher.services;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.URI;
import java.util.function.Consumer;

@Configuration
@Slf4j
public class InfuraSocketConfiguration {

    @Autowired
    private Web3jService web3jService;

    @Bean("web3jSocketConnection")
    @SneakyThrows
    Web3j webSocketSubscription() {
        final WebSocketClient webSocketClient = new WebSocketClient(new URI(web3jService.getInfuraWsEndpoint()));
        final boolean includeRawResponses = false;
        final WebSocketService webSocketService = new WebSocketService(webSocketClient, includeRawResponses);
        webSocketClient.connect();
        return Web3j.build(webSocketService);
    }

    private Consumer<String> logger() {
        return (String s) -> log.info("Logging web3j subscription: " + s);
    }
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


}
