package com.clarity.transactiondispatcher.web.handler;

import an.awesome.pipelinr.Command;
import com.clarity.clarityshared.Query;
import com.clarity.transactiondispatcher.services.EthereumOperations;
import com.clarity.transactiondispatcher.utils.JSONAble;
import com.clarity.transactiondispatcher.web.controller.ResponseFactory;
import com.clarity.transactiondispatcher.web.model.AccountBalanceRequestDTO;
import com.clarity.transactiondispatcher.web.model.AccountRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.util.Base64;
import org.springframework.stereotype.Component;
import org.web3j.crypto.WalletFile;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class EthereumAccountGetBalance implements Query<Mono<Map<String, Object>>> {
    @Getter
    private AccountBalanceRequestDTO accountBalanceRequestDTO;
    @Getter
    private EthereumOperations operations;

    @Component
    @NoArgsConstructor
    static class Handler implements Command.Handler<EthereumAccountGetBalance, Mono<Map<String, Object>>>, ResponseFactory, JSONAble {
        @Override
        public Mono<Map<String, Object>> handle(EthereumAccountGetBalance command) {
            Mono<Map<String, Object>> result = null;
            try {
               String wallet = command.getAccountBalanceRequestDTO().getWallet();
                ObjectMapper objectMapper = new ObjectMapper();
                final WalletFile walletFile = objectMapper.readValue(new String(Base64.decode(wallet)), WalletFile.class);


                final BigInteger balance = command.getOperations().getBalance(walletFile);

                Map<String, BigInteger> balanceResult = new HashMap<>();
                balanceResult.put("balance", balance);

                result = getSuccessResponse(balanceResult);
            } catch (Exception ex) {
                log.info(ex.getMessage());
            }
            return result;
        }
    }





}
