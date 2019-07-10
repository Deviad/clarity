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

    @Override
    public void afterPropertiesSet() {
        started();
    }

    public static void main(String[] args) {
        SpringApplication.run(TransactionDispatcherApplication.class, args);
    }

}
