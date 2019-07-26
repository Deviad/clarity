package com.clarity.transactiondispatcher.web.controller;


import com.clarity.transactiondispatcher.services.EthereumOperations;
import com.clarity.transactiondispatcher.services.PipelinrService;
import com.clarity.transactiondispatcher.services.Web3jService;
import com.clarity.transactiondispatcher.web.handler.EthereumAccountCreate;
import com.clarity.transactiondispatcher.web.handler.EthereumAccountGetBalance;
import com.clarity.transactiondispatcher.web.model.AccountBalanceRequestDTO;
import com.clarity.transactiondispatcher.web.model.AccountRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.util.Base64;
import org.jooq.lambda.Unchecked;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EthereumAccountController {


    private final PipelinrService pipelinrService;
    private final EthereumOperations operations;
    @Qualifier("web3jSocketConnection") private final Web3j web3jSocketConnection;
    private final Web3jService web3jService;


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


//        web3jSocketConnection
//                .transactionFlowable()
////                .map(transaction -> transaction.getTo())
//                .doOnSubscribe(
//                    subscription -> log.info("Subscribe to newly transactions confirmed on the blockchain."))
//                .filter(transaction-> transaction.getTo().equals(supplierCompletableFuture.get().get().getAddress()))
//                .subscribe(
//                        transaction-> log.info("Transaction from, to, gasPrice {}, {}, {}", transaction.getFrom(), transaction.getTo(), transaction.getGasPrice()),
//                        throwable -> log.error("Could not subscribe to block notifications: {}", throwable.getMessage())
//                );
        return pipelinrService.getQueryPipeline().send(new EthereumAccountGetBalance(accountBalanceRequestDTO, operations));
    }
}
