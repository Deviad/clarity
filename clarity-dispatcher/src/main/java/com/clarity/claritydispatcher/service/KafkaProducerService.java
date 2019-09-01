package com.clarity.claritydispatcher.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micronaut.context.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.Properties;
import java.util.concurrent.Future;

@Singleton
@Slf4j
public class KafkaProducerService {

    Properties properties = new Properties();

    @Value("${kafka.bootstrap.servers}")
    private
    String kafkaBootstrapServers;

    private Producer<String, String> producer;

    @PostConstruct
    void init() {
        log.info("kafkaBootstrapServers: {}", kafkaBootstrapServers);
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        properties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "clarity-dispatcher");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // producer acks

        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // strongest producing guarantee
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
        // leverage idempotent producer from Kafka 0.11 !
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // ensure we don't push duplicates
        producer = new KafkaProducer<>(properties);
    }

    public Future<RecordMetadata> sendRecord(String topic, String key, String value) {
       return producer.send(buildRecord(topic, key, value));
    }

    private ProducerRecord<String, String> buildRecord(String topic, String key, String value) {
        ObjectNode transaction = JsonNodeFactory.instance.objectNode();
        transaction.put(key, value);
        return new ProducerRecord<>(topic, key, transaction.toString());

    }

}
