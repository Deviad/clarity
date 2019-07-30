package com.clarity.transactiondispatcher.web.handler;

import an.awesome.pipelinr.Command;
import com.clarity.clarityshared.Query;
import com.clarity.transactiondispatcher.services.EthereumService;
import com.clarity.transactiondispatcher.utils.JSONAble;
import com.clarity.transactiondispatcher.web.controller.ResponseFactory;
import com.clarity.transactiondispatcher.web.model.AccountBalanceRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.util.Base64;
import org.jooq.lambda.Unchecked;
import org.springframework.stereotype.Component;
import org.web3j.crypto.WalletFile;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
public class EthereumAccountGetBalance implements Query<Mono<Map<String, Object>>> {
    @Getter
    private AccountBalanceRequestDTO accountBalanceRequestDTO;
    @Getter
    private EthereumService ethService;

    @Component
    @NoArgsConstructor
    static class Handler implements Command.Handler<EthereumAccountGetBalance, Mono<Map<String, Object>>>, ResponseFactory, JSONAble {
        @Override
        public Mono<Map<String, Object>> handle(EthereumAccountGetBalance command) {
               String wallet = command.getAccountBalanceRequestDTO().getWallet();
                ObjectMapper objectMapper = new ObjectMapper();

                final CompletableFuture<Supplier<WalletFile>> supplierCompletableFuture = CompletableFuture
                        .supplyAsync(() -> Unchecked.supplier(
                                () -> objectMapper.readValue(new String(Unchecked.supplier(
                                        () -> Base64.decode(wallet)).get()), WalletFile.class)));

                final CompletableFuture<Mono<Map<String, Object>>> walletFile = supplierCompletableFuture
                        .thenApply(Supplier::get)
                        .thenApply(x-> command.getEthService().getBalance(x))
                        .thenApply(b-> {
                            Map<String, BigInteger> balanceResult = new HashMap<>();
                            balanceResult.put("balance", b);
                            return balanceResult;
                        })
                        .thenApply(this::getSuccessResponse)
                        .exceptionally(this::getJsonErrsResp);

                return Unchecked.supplier(walletFile::get).get();

        }



    }





}
