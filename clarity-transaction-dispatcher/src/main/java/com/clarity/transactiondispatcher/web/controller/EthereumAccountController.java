package com.clarity.transactiondispatcher.web.controller;


import com.clarity.transactiondispatcher.services.EthereumClient;
import com.clarity.transactiondispatcher.services.EthereumOperations;
import com.clarity.transactiondispatcher.services.PipelinrService;
import com.clarity.transactiondispatcher.web.handler.EthereumAccountCreate;
import com.clarity.transactiondispatcher.web.handler.EthereumAccountGetBalance;
import com.clarity.transactiondispatcher.web.model.AccountBalanceRequestDTO;
import com.clarity.transactiondispatcher.web.model.AccountRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.util.Base64;
import org.jooq.lambda.Unchecked;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.WalletFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EthereumAccountController {


    private final PipelinrService pipelinrService;
    private final EthereumOperations operations;
    private final EthereumClient ethereumClient;

    @PostMapping("/ethaccount")
    @SneakyThrows
    public Mono<Map<String, Object>> createAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        return pipelinrService.getQueryPipeline().send(new EthereumAccountCreate(accountRequestDTO, operations));
    }

    @PostMapping("/ethaccount/balance")
    @SneakyThrows
    public Mono<Map<String, Object>> getAccountBalance(@RequestBody @Valid AccountBalanceRequestDTO accountBalanceRequestDTO) {
        return pipelinrService.getQueryPipeline().send(new EthereumAccountGetBalance(accountBalanceRequestDTO, operations));
    }

    @PostMapping("/ethaccount/updatedbalance")
    @SneakyThrows
    public Mono<Map<String, Object>> getUpdatedAccountBalance(@RequestBody @Valid AccountBalanceRequestDTO accountBalanceRequestDTO) {

        ObjectMapper objectMapper = new ObjectMapper();

        final CompletableFuture<Supplier<WalletFile>> supplierCompletableFuture = CompletableFuture
                .supplyAsync(() -> Unchecked.supplier(
                        () -> objectMapper.readValue(new String(Unchecked.supplier(
                                () -> Base64.decode(accountBalanceRequestDTO.getWallet())).get()), WalletFile.class)));

        return pipelinrService.getQueryPipeline().send(new EthereumAccountGetBalance(accountBalanceRequestDTO, operations));
    }

//    @GetMapping(value = "/ethaccount/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @MessageMapping("/ethaccount/test")
    public Flux<String> getTest() {
        Map<String, Object> map = Stream.of(
                new AbstractMap.SimpleEntry<>("id", 1),
                new AbstractMap.SimpleEntry<>("method", "eth_subscribe"),
                new AbstractMap.SimpleEntry<>("params", new Object[]{"newHeads"})
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Flux.from(ethereumClient.request(map));
    }
}
