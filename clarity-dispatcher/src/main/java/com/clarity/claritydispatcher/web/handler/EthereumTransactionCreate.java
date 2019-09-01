package com.clarity.claritydispatcher.web.handler;


import an.awesome.pipelinr.Command;
import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.ResponseFactory;
import com.clarity.claritydispatcher.web.model.TransactionRequestDTO;
import com.clarity.clarityshared.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.util.Base64;
import org.web3j.crypto.WalletFile;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class EthereumTransactionCreate implements Query<Mono<Map<String, Object>>> {
    @Getter
    private TransactionRequestDTO transactionRequestDTO;
    @Getter
    private EthereumService ethService;

    @NoArgsConstructor
 public static class Handler implements Command.Handler<EthereumTransactionCreate, Mono<Map<String, Object>>>, ResponseFactory {
        @Override
        @SneakyThrows
        public Mono<Map<String, Object>> handle(EthereumTransactionCreate command) {
            Mono<Map<String, Object>> result = null;
            try {
                String password = command.transactionRequestDTO.getPassword();
                String walletBase64 = command.transactionRequestDTO.getWallet();
                BigDecimal amount = command.transactionRequestDTO.getAmount();
                ObjectMapper objectMapper = new ObjectMapper();
                WalletFile walletFile = objectMapper.readValue(new String(Base64.decode(walletBase64)), WalletFile.class);

                result = getSuccessResponse(command.ethService.sendTransaction(password, walletFile, amount, command.transactionRequestDTO.getToAddress()));
            } catch (Exception ex) {
                log.info(ex.getMessage());
            }
            return result;
        }
    }
}
