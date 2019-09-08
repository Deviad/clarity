package com.clarity.claritydispatcher.web.controller;


import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.KafkaMessageListener;
import com.clarity.claritydispatcher.service.KafkaService;
import com.clarity.claritydispatcher.service.PipelinrService;
import com.clarity.claritydispatcher.web.handler.HyperledgerAccountHandler;
import com.clarity.claritydispatcher.web.model.AccountRequestDTO;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@Controller(value = "/hyperaccount")
@AllArgsConstructor
public class HyperledgerAccountController {
    private final PipelinrService pipelinrService;
    private final EthereumService ethService;
    private final KafkaService kafkaservice;
    private final KafkaMessageListener kafkaListener;


    @Post(value = "/function/create", processes = MediaType.APPLICATION_JSON)
    public Observable<Map<String, Object>> createAccount(@Valid AccountRequestDTO accountRequestDTO) {
        return this.pipelinrService.getCommandPipeline().orElseThrow().send(new HyperledgerAccountHandler(accountRequestDTO, kafkaservice, kafkaListener));
    }

}
