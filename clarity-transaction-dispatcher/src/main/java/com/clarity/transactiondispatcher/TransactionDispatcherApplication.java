package com.clarity.transactiondispatcher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;

import java.util.TimeZone;

@SpringBootApplication
//@EnableBinding(KStreamProcessorX.class)
public class TransactionDispatcherApplication implements InitializingBean {


    private void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void afterPropertiesSet() {
        started();
    }

    public static void main(String[] args) {
        SpringApplication.run(TransactionDispatcherApplication.class, args);
    }

}
