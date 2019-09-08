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
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class HyperledgerAccountHandler implements Query<Observable<Map<String, Object>>>, ResponseFactory {

    @Getter
    private AccountRequestDTO accountRequestDTO;
    @Getter
    private KafkaService kafkaProducer;
    @Getter
    private KafkaMessageListener kafkaMessageListener;

    public static class Handler implements Command.Handler<HyperledgerAccountHandler, Observable<Map<String, Object>>>, ResponseFactory {


        @SneakyThrows
        private static Observable<Map<String, Object>> getResult(HyperledgerAccountHandler command) {

//            final CountDownLatch latch = new CountDownLatch(30);
//            while (!latch.await(10, TimeUnit.SECONDS)) {
//                if (command.kafkaMessageListener.messages.peek() == null) {
//                    latch.countDown();
//                } else {
//                    return command.getSuccessMonoResponse(command.kafkaMessageListener.messages.poll());
//                }
//            }
            final BehaviorSubject<String> subject = command.kafkaMessageListener.bus.getEvents();

            return Maybe.<String>create(emitter -> {
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
//                    .observeOn(Schedulers.single())
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
                            return Observable.error(new NoMessageReceivedException("No message from hyperledger"));
                        }
                    }))
                    .doOnError(throwable -> Observable.error(new NoMessageReceivedException("No message from hyperledger")))
                    .map(command::getSuccessScalarResponse);


//            return command.kafkaMessageListener.bus.getEvents()
//                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
//                    .observeOn(io.reactivex.schedulers.Schedulers.single())
////                    .retry(3, (throwable) -> throwable instanceof NoMessageReceivedException)
//                    .switchIfEmpty(Observable.error(new NoMessageReceivedException("Faiga")))
//                    .doOnError(e -> log.info("{}", e.getMessage()))
//                    .doOnSubscribe(x -> log.info("FF, {}", x.toString()))
//                    .retryWhen(attempts -> attempts.zipWith(Observable.range(1, 4), (n, i) -> i).flatMap(i -> {
//                        log.info("delay retry by {} second(s)", i);
////                        if (i == 4) {
////                            try {
////                                throw new NoMessageReceivedException("Could not receive a message from Hyperledger: tried" + i + "times");
////                            } catch (Exception ex) {
////                            }
////                        }
//                        return Observable.interval(i, TimeUnit.SECONDS);
//                    }))
//                    .reduce((first, last) -> first).blockingGet();


//            throw new NoMessageReceivedException("No message received from Hyperledger");
                    }

            @Override
            public Observable<Map<String, Object>> handle (HyperledgerAccountHandler command){
                command.kafkaProducer.publishSentence(
                        command.accountRequestDTO.getUsername(),
                        Single.just(command.accountRequestDTO.getUsername()));

                return getResult(command);
            }
        }
    }
