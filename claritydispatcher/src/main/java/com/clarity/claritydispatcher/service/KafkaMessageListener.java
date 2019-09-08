package com.clarity.claritydispatcher.service;

import com.clarity.claritydispatcher.MyRxOutputBean;
import com.clarity.clarityshared.Topics;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Context;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
@Context
@KafkaListener(groupId = "clarity-dispatcher-reply", offsetReset = OffsetReset.EARLIEST)
public class KafkaMessageListener {
    public Queue<String> messages = new ConcurrentLinkedDeque<>();
    public MyRxOutputBean<String> bus = new MyRxOutputBean<>();

    @Topic(value = Topics.CREATE_HYPERLEDGER_WALLET_REPLY)
    public void receive(@KafkaKey String username, Single<String> message) {
        log.info("Got New Request - {} by {}", username, message.blockingGet());
        messages.offer(message.blockingGet());
//        bus.setObject(message.blockingGet());
    }
}
