package com.clarity.claritydispatcher.web.handler;

import an.awesome.pipelinr.Command;
import com.clarity.claritydispatcher.error.exceptions.NoMessageReceivedException;
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


        @Override
        public Mono<Map<String, Object>> handle(HyperledgerAccountHandler command) {
            command.kafkaProducer.publishSentence(
                    command.accountRequestDTO.getUsername(),
                    Single.just(command.accountRequestDTO.getUsername()));

            return getResult(command);

        }

        @SneakyThrows
        private static Mono<Map<String, Object>> getResult(HyperledgerAccountHandler command) {

            final CountDownLatch latch = new CountDownLatch(30);
            while (!latch.await(10, TimeUnit.SECONDS)) {
                if (command.kafkaMessageListener.messages.peek() == null) {
                    latch.countDown();
                } else {
                    return command.getSuccessMonoResponse(command.kafkaMessageListener.messages.poll());
                }
            }

            throw new NoMessageReceivedException("No message received from Hyperledger");
        }
    }
}
