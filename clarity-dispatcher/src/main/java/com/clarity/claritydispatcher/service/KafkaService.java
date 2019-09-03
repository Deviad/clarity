package com.clarity.claritydispatcher.service;

import com.clarity.clarityshared.Topics;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.Properties;
import java.util.concurrent.Future;

@Singleton
@KafkaClient(
        id="clarity-dispatcher",
        acks = KafkaClient.Acknowledge.ALL,
        properties = @Property(name = ProducerConfig.RETRIES_CONFIG, value = "5")
)
public interface KafkaService {

    @Topic(Topics.CREATE_HYPERLEDGER_WALLET_INPUT)
    void publishSentence(@KafkaKey String username, String message);
}
