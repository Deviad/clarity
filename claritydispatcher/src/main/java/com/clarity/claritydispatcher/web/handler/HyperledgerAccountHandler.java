package com.clarity.claritydispatcher.web.handler;

import an.awesome.pipelinr.Command;
import com.clarity.claritydispatcher.error.NoMessageReceived;
import com.clarity.claritydispatcher.service.KafkaMessageListener;
import com.clarity.claritydispatcher.service.KafkaService;
import com.clarity.claritydispatcher.service.ResponseFactory;
import com.clarity.claritydispatcher.web.model.AccountRequestDTO;
import com.clarity.clarityshared.Query;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class HyperledgerAccountHandler implements Query<Mono<Map<String, Object>>>, ResponseFactory {

    @Getter
    private AccountRequestDTO accountRequestDTO;
    @Getter
    private KafkaService kafkaProducer;
    @Getter
    private KafkaMessageListener kafkaMessageListener;

    public static class Handler implements Command.Handler<HyperledgerAccountHandler, Mono<Map<String, Object>>>, ResponseFactory {

        @SneakyThrows
        private static Mono<Map<String, Object>> getResult(HyperledgerAccountHandler command) {

            final CountDownLatch latch = new CountDownLatch(10);
            while (!latch.await(30, TimeUnit.SECONDS)) {
                if (command.kafkaMessageListener.messages.peek() == null) {
                    latch.countDown();
                } else {
                    return command.getSuccessResponse(command.kafkaMessageListener.messages.peek());
                }
            }

            throw new NoMessageReceived("No message received from Hyperledger");
        }

        @Override
        public Mono<Map<String, Object>> handle(HyperledgerAccountHandler command) {
            command.kafkaProducer.publishSentence(
                    command.accountRequestDTO.getUsername(),
                    Single.just(command.accountRequestDTO.getUsername()));

            return getResult(command);

        }
    }
}
