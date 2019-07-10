package com.clarity.transactiondispatcher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class TransactionDispatcherApplication implements InitializingBean {


    private void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(TransactionDispatcherApplication.class, args);
    }

    @Override
    public void afterPropertiesSet() {
        started();
    }

}
