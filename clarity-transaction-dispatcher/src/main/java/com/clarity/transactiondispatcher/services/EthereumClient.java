package com.clarity.transactiondispatcher.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

@Component
@Slf4j
public class EthereumClient {

 private final ReactorNettyWebSocketClient client;
 private final EmitterProcessor<String> otuput;
 private final Web3jService web3jService;

    public EthereumClient(ReactorNettyWebSocketClient client, EmitterProcessor<String> otuput, Web3jService web3jService) {
        this.client = client;
        this.otuput = otuput;
        this.web3jService = web3jService;
    }


    public Flux<String> request(final Map<String, Object> reqParams) {
        ObjectMapper objectMapper = new ObjectMapper();

        String json = Unchecked.supplier(()->objectMapper.writeValueAsString(reqParams)).get();


        final Mono<Void> sessionMono = sessionMonoFactory(inputFactory(json));

        return otuput.doOnSubscribe(s -> sessionMono.subscribe()).doOnError(x -> log.info(x.getMessage()));
    }


    private Flux<String> inputFactory(String json) {
        return Flux.<String>generate(sink -> sink.next(json))
                .delayElements(Duration.ofSeconds(1));
    }

    private Mono<Void> sessionMonoFactory(Flux<String> input) {
       return client.execute(URI.create(web3jService.getInfuraWsEndpoint()), session -> session.send(input.map(session::textMessage))
                .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText).subscribeWith(otuput).then()).then());
    }

}
