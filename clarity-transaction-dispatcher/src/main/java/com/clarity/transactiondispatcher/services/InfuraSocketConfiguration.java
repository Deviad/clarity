package com.clarity.transactiondispatcher.services;


import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.protocol.websocket.events.NewHeadsNotification;

import java.net.URI;
import java.util.function.Consumer;

@Configuration
@Slf4j
public class InfuraSocketConfiguration {

    @Autowired
    private Web3jService web3jService;

    @Bean("webSocketSubscription")
    @SneakyThrows
    Disposable webSocketSubscription() {
        final WebSocketClient webSocketClient = new WebSocketClient(new URI(web3jService.getInfuraWsEndpoint()));
        final boolean includeRawResponses = false;
        final WebSocketService webSocketService = new WebSocketService(webSocketClient, includeRawResponses);

        final Web3j web3j = Web3j.build(webSocketService);
        final Flowable<NewHeadsNotification> notifications = web3j.newHeadsNotifications();
        Consumer<String> loggerProducer = (String s) -> log.info("Logging web3j subscription: " + s);
        return notifications.subscribe(x -> loggerProducer.accept(x.getJsonrpc()));
    }
}
