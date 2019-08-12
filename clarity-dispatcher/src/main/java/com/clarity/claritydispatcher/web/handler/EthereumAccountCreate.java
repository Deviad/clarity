package com.clarity.claritydispatcher.web.handler;

import an.awesome.pipelinr.Command;
import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.QueryPipeline;
import com.clarity.claritydispatcher.service.ResponseFactory;
import com.clarity.claritydispatcher.web.model.AccountRequestDTO;
import com.clarity.clarityshared.Query;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.context.annotation.Requires;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class EthereumAccountCreate implements Query<Mono<Map<String, Object>>>, ResponseFactory {
  @Getter private AccountRequestDTO accountRequestDTO;
  @Getter private EthereumService ethService;


  @NoArgsConstructor
public  static class Handler
      implements Command.Handler<EthereumAccountCreate, Mono<Map<String, Object>>>,
          ResponseFactory {
    @Override
    public Mono<Map<String, Object>> handle(EthereumAccountCreate command) {
      Mono<Map<String, Object>> result = null;
      try {
        String password = command.accountRequestDTO.getPassword();
        final Map<String, String> walletInfo = command.getEthService().createAccount(password);
        result = getSuccessResponse(walletInfo);
      } catch (Exception ex) {
        log.info(ex.getMessage());
      }
      return result;
    }
  }
}
