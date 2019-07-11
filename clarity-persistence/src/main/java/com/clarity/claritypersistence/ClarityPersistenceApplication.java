package com.clarity.claritypersistence;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class ClarityPersistenceApplication implements InitializingBean {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ClarityPersistenceApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        SpringApplication.run(ClarityPersistenceApplication.class, args);
    }

    private void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void afterPropertiesSet() {
        started();
    }

}
