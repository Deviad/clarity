package com.clarity.transactiondispatcher.web.controller;


import an.awesome.pipelinr.Pipeline;
import com.clarity.transactiondispatcher.services.Web3jService;
import com.clarity.transactiondispatcher.web.handler.EthereumAccountCreate;
import com.clarity.transactiondispatcher.web.model.AccountRequestDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
public class EthereumAccountController implements ResponseFactory {

    private final Pipeline queryPipeline;

    private final Pipeline commandPipeline;

    private final Web3jService web3;


    public EthereumAccountController(
            @Qualifier("queryPipelinr") Pipeline queryPipeline,
            @Qualifier("commandPipelinr") Pipeline commandPipeline,
            Web3jService web3) {
        this.queryPipeline = queryPipeline;
        this.commandPipeline = commandPipeline;
        this.web3 = web3;
    }
    @PostMapping("/ethaccount")
    @SneakyThrows
    public Mono<Map<String, Object>> createAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
       return queryPipeline.send(new EthereumAccountCreate(accountRequestDTO)).get();
    }
}
