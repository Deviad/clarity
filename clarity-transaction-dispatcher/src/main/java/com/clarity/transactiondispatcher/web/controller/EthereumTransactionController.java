package com.clarity.transactiondispatcher.web.controller;

import com.clarity.transactiondispatcher.services.EthereumService;
import com.clarity.transactiondispatcher.services.PipelinrService;
import com.clarity.transactiondispatcher.web.handler.EthereumTransactionCreate;
import com.clarity.transactiondispatcher.web.model.TransactionRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
public class EthereumTransactionController {

    private final PipelinrService pipelinrService;
    private final EthereumService ethereumService;

    public EthereumTransactionController(PipelinrService pipelinrService, EthereumService ethereumService) {
        this.pipelinrService = pipelinrService;
        this.ethereumService = ethereumService;
    }

    @PostMapping("/ethtransaction")
    public Mono<Map<String, Object>> createTransaction(
            @RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
        return pipelinrService.getQueryPipeline()
                .send(new EthereumTransactionCreate(transactionRequestDTO, ethereumService));
    }

}
