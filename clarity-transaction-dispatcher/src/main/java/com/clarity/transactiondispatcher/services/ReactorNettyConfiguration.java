package com.clarity.transactiondispatcher.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.EmitterProcessor;

@Configuration
@Slf4j
public class ReactorNettyConfiguration {

    @Bean
    ReactorNettyWebSocketClient client() {
        return new ReactorNettyWebSocketClient();
    }

    @Bean
    EmitterProcessor<String> emitterProcessor() {
        return EmitterProcessor.create();
    }
}
