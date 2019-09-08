package com.clarity.claritydispatcher.web.handler;

import an.awesome.pipelinr.Command;
import com.clarity.claritydispatcher.error.exceptions.NoMessageReceivedException;
import com.clarity.claritydispatcher.service.KafkaMessageListener;
import com.clarity.claritydispatcher.service.KafkaService;
import com.clarity.claritydispatcher.service.ResponseFactory;
import com.clarity.claritydispatcher.web.model.AccountRequestDTO;
import com.clarity.clarityshared.Query;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class HyperledgerAccountHandler implements Query<Map<String, Object>>, ResponseFactory {

    @Getter
    private AccountRequestDTO accountRequestDTO;
    @Getter
    private KafkaService kafkaProducer;
    @Getter
    private KafkaMessageListener kafkaMessageListener;

    public static class Handler implements Command.Handler<HyperledgerAccountHandler, Map<String, Object>>, ResponseFactory {

        @SneakyThrows
        private static Map<String, Object> getResult(HyperledgerAccountHandler command) {

//            final CountDownLatch latch = new CountDownLatch(30);
//            while (!latch.await(10, TimeUnit.SECONDS)) {
//                if (command.kafkaMessageListener.messages.peek() == null) {
//                    latch.countDown();
//                } else {
//                    return command.getSuccessMonoResponse(command.kafkaMessageListener.messages.poll());
//                }
//            }
            final BehaviorSubject<String> subject = command.kafkaMessageListener.bus.getEvents();
            Map<String, Object> result;
            try {
                result = Maybe.<String>create(emitter -> {
                    try {
                        if (subject.getValue() != null) {
                            emitter.onSuccess(subject.getValue());
                        } else {
                            emitter.onComplete();
                        }
                    } catch (Throwable ex) {
                        emitter.onError(ex);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .toObservable()
                        .switchIfEmpty(Observable.create(emitter ->
                        {
                            emitter.onError(new NoMessageReceivedException("No message from hyperledger"));

                        }))
                        .retryWhen(attempts -> attempts.zipWith(Observable.range(1, 3), (error, i) ->
                        {
                            if (i < 3) {
                                return Observable.timer(i, TimeUnit.SECONDS);
                            } else {
                                return Observable.error(error);
                            }
                        }))
                        .observeOn(Schedulers.single())
                        .map(command::getSuccessScalarResponse).blockingFirst();
            } catch (NoSuchElementException ex) {
                throw new NoMessageReceivedException("No message from hyperledger");
            }
            return result;
        }

        @Override
        public Map<String, Object> handle(HyperledgerAccountHandler command) {
            command.kafkaProducer.publishSentence(
                    command.accountRequestDTO.getUsername(),
                    Single.just(command.accountRequestDTO.getUsername()));

            return getResult(command);
        }
    }
}
