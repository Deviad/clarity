package com.clarity.claritydispatcher.service;

import com.clarity.clarityshared.Topics;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@KafkaListener(offsetReset = OffsetReset.EARLIEST)
public class KafkaMessageListener {

    @Topic(value = Topics.CREATE_HYPERLEDGER_WALLET)
    public void receive(@KafkaKey String username, String message) {
        log.info("Got New Request - {} by {}", username, message);
    }
}
