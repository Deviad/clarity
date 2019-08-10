package com.clarity.claritydispatcher.web.handler;

import an.awesome.pipelinr.Command;
import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.ResponseFactory;
import com.clarity.claritydispatcher.web.model.AccountBalanceRequestDTO;
import com.clarity.clarityshared.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.util.Base64;
import org.jooq.lambda.Unchecked;
import org.web3j.crypto.WalletFile;
import reactor.core.publisher.Mono;

import javax.inject.Singleton;
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

  @Singleton
  @NoArgsConstructor
  static class Handler implements Command.Handler<EthereumAccountGetBalance, Mono<Map<String, Object>>>, ResponseFactory, com.clarity.util.JSONAble {
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

