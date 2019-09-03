package com.clarity.clarityhyperledger;

import com.clarity.clarityshared.Topics;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface IKafkaClient {

    @Topic(Topics.CREATE_HYPERLEDGER_WALLET_REPLY)
    void publishSentence(@KafkaKey String key, String message);
}
