package com.clarity.claritydispatcher.web.handler;

import an.awesome.pipelinr.Command;
import com.clarity.claritydispatcher.service.EthereumService;
import com.clarity.claritydispatcher.service.KafkaProducerService;
import com.clarity.claritydispatcher.service.ResponseFactory;
import com.clarity.claritydispatcher.web.model.AccountRequestDTO;
import com.clarity.clarityshared.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.clarity.clarityshared.Topics.CREATE_HYPERLEDGER_WALLET;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class EthereumAccountCreate implements Query<Mono<Map<String, Object>>>, ResponseFactory {
    @Getter
    private AccountRequestDTO accountRequestDTO;
    @Getter
    private EthereumService ethService;
    @Getter
    private KafkaProducerService kafkaProducer;
    @NoArgsConstructor
    public static class Handler
            implements Command.Handler<EthereumAccountCreate, Mono<Map<String, Object>>>,
            ResponseFactory {
        @Override
        public Mono<Map<String, Object>> handle(EthereumAccountCreate command) {
           Mono<Map<String, Object>> result = null;
                try {
                    String password = command.accountRequestDTO.getPassword();
                    final Map<String, String> walletInfo = command.getEthService().createAccount(password);

                    command.kafkaProducer.sendRecord(
                            CREATE_HYPERLEDGER_WALLET,
                            command.accountRequestDTO.getUsername(),
                            command.accountRequestDTO.getUsername());
                    result = getSuccessResponse(walletInfo);
            } catch (Exception ex) {
                log.info(ex.getMessage());
            }
            return result;
        }
    }
}
