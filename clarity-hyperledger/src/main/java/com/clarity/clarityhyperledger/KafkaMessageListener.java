package com.clarity.clarityhyperledger;

import com.clarity.clarityshared.Topics;
import io.micronaut.configuration.kafka.annotation.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
@KafkaListener(offsetReset = OffsetReset.EARLIEST)
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessageListener {
   @Inject
   IKafkaClient kafkaClient;

    @Topic(value = Topics.CREATE_HYPERLEDGER_WALLET_INPUT)
    public void receive(@KafkaKey String username, String message) {
        log.info("Got New Request - {} by {}", username, message);
        kafkaClient.publishSentence(username, message + "response");
    }
}
