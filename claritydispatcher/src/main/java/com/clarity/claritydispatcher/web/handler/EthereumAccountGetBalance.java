package com.clarity.claritydispatcher.web.handler;

import an.awesome.pipelinr.Command;
import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.ResponseFactory;
import com.clarity.claritydispatcher.util.JSONAble;
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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class EthereumAccountGetBalance implements Query<Mono<Map<String, Object>>> {
  @Getter private AccountBalanceRequestDTO accountBalanceRequestDTO;
  @Getter private EthereumService ethService;

  @NoArgsConstructor
 public static class Handler
      implements Command.Handler<EthereumAccountGetBalance, Mono<Map<String, Object>>>,
          ResponseFactory,
          JSONAble {
    @Override
    public Mono<Map<String, Object>> handle(EthereumAccountGetBalance command) {
      String wallet = command.getAccountBalanceRequestDTO().getWallet();
      ObjectMapper objectMapper = new ObjectMapper();

      final CompletableFuture<Supplier<WalletFile>> supplierCompletableFuture =
          CompletableFuture.supplyAsync(
              () ->
                  Unchecked.supplier(
                      () ->
                          objectMapper.readValue(
                              new String(Unchecked.supplier(() -> Base64.decode(wallet)).get()),
                              WalletFile.class)));

      final CompletableFuture<Mono<Map<String, Object>>> walletFile =
          supplierCompletableFuture
              .thenApply(Supplier::get)
              .thenApply(x -> command.getEthService().getBalance(x))
              .thenApply(
                  b -> {
                    Map<String, BigInteger> balanceResult = new HashMap<>();
                    balanceResult.put("balance", b);
                    return balanceResult;
                  })
              .thenApply(this::getSuccessMonoResponse)
              .exceptionally(this::getJsonErrMonoResp);
      return walletFile.join();
    }
  }
}
