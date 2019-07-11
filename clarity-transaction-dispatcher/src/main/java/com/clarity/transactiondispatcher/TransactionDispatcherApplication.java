package com.clarity.transactiondispatcher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.util.TimeZone;

@SpringBootApplication(exclude = WebMvcAutoConfiguration.class)
//@EnableBinding(KStreamProcessorX.class)
public class TransactionDispatcherApplication implements InitializingBean {


    private void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void afterPropertiesSet() {
        started();
    }

    public static void main(String ...args) {
        new SpringApplicationBuilder(TransactionDispatcherApplication.class)
                .web(WebApplicationType.REACTIVE)
                .run(args);

    }
}
