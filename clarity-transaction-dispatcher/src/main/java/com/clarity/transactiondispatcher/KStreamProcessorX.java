package com.clarity.transactiondispatcher;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface KStreamProcessorX {

    @Input("input")
    KStream<?, ?> input();

    @Output("output1")
    KStream<?, ?> output1();


}
