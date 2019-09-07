package com.clarity.claritydispatcher.service;

import com.clarity.clarityshared.Topics;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Property;
import io.reactivex.Single;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Singleton
@KafkaClient(
        id="clarity-dispatcher",
        acks = KafkaClient.Acknowledge.ALL,
        properties = @Property(name = ProducerConfig.RETRIES_CONFIG, value = "5")
)
public interface KafkaService {

    @Topic(Topics.CREATE_HYPERLEDGER_WALLET_INPUT)
   RecordMetadata  publishSentence(@KafkaKey String username, Single<String> message);
}
