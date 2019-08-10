package com.clarity.clarityeurekaserver;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.util.TimeZone;

@SpringBootApplication
@EnableEurekaServer
public class ClarityEurekaServerApplication implements InitializingBean {

    private void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public void afterPropertiesSet() {
        started();
    }

    public static void main(String[] args) {
        SpringApplication.run(ClarityEurekaServerApplication.class, args);
    }

}
