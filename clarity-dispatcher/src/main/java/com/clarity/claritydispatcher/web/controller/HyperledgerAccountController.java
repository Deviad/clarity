package com.clarity.claritydispatcher.web.controller;


import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.KafkaService;
import com.clarity.claritydispatcher.service.PipelinrService;
import io.micronaut.http.annotation.Controller;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Controller(value = "/hyperaccount")
@AllArgsConstructor
public class HyperledgerAccountController {
    private final PipelinrService pipelinrService;
    private final EthereumService ethService;
    private final KafkaService kafkaservice;

    public Mono<String> createAccount() {
        return Mono.just("whatever");
    }

}
