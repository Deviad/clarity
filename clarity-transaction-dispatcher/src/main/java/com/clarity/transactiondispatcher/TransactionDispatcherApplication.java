package com.clarity.transactiondispatcher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.util.TimeZone;

@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class})

@EnableWebFlux
public class TransactionDispatcherApplication implements InitializingBean {

    private void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void afterPropertiesSet() {
        started();
    }

    public static void main(String... args) {
        new SpringApplicationBuilder(TransactionDispatcherApplication.class).web(WebApplicationType.REACTIVE).run(args);

    }
}
