package com.clarity.transactiondispatcher.web.handler;


import an.awesome.pipelinr.Command;
import com.clarity.clarityshared.Query;
import com.clarity.transactiondispatcher.services.EthereumOperations;
import com.clarity.transactiondispatcher.web.controller.ResponseFactory;
import com.clarity.transactiondispatcher.web.model.AccountRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class EthereumAccountCreate implements Query<Mono<Map<String, Object>>> {
    @Getter
    private AccountRequestDTO accountRequestDTO;
    @Getter
    private EthereumOperations operations;

    @Component
    @NoArgsConstructor
    static class Handler implements Command.Handler<EthereumAccountCreate, Mono<Map<String, Object>>>, ResponseFactory {
        @Override
        public Mono<Map<String, Object>> handle(EthereumAccountCreate command) {
            Mono<Map<String, Object>> result = null;
            try {
                String password = command.accountRequestDTO.getPassword();
                final Map<String, String> walletInfo = command.getOperations().createAccount(password);
                result = getSuccessResponse(walletInfo);
            } catch (Exception ex) {
                log.info(ex.getMessage());
            }
            return result;
        }
    }
}
