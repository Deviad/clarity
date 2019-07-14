package com.clarity.transactiondispatcher.web.handler;


import com.clarity.clarityshared.Query;
import com.clarity.transactiondispatcher.web.controller.ResponseFactory;
import com.clarity.transactiondispatcher.web.model.AccountRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class EthereumAccountCreate implements Query<CompletableFuture<Mono<Map<String, Object>>>>, ResponseFactory {

    private Mono<Map<String, Object>> result = null;
    public EthereumAccountCreate(AccountRequestDTO accountRequestDTO) {
        this.init(accountRequestDTO);
    }

    private void init(AccountRequestDTO accountRequestDTO) {

        //TODO: add global errorApi and use PipeLinr to keep the controller clean
        try {
            String password = accountRequestDTO.getPassword();
            ECKeyPair keyPair = Keys.createEcKeyPair();
            WalletFile wallet = Wallet.createStandard(password, keyPair);
            Map<String, String> walletInfo = new LinkedHashMap<>();
            walletInfo.put("keyPair", keyPair.getPrivateKey().toString(16));
            walletInfo.put("address", wallet.getAddress());
            walletInfo.put("id", wallet.getId());
            result = getJsonSuccessResp(walletInfo);
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }

    }

    private Mono<Map<String, Object>> getResult() {
        return result;
    }

    @Component
    static class Handler implements Query.Handler<EthereumAccountCreate, CompletableFuture<Mono<Map<String, Object>>>> {

        @Override
        public CompletableFuture<Mono<Map<String, Object>>> handle(EthereumAccountCreate command) {

            return CompletableFuture.completedFuture(command.getResult());
        }
    }
}
