package com.clarity.transactiondispatcher.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    DefaultServerCodecConfigurer defaultServerCodecConfigurer() {
        return new DefaultServerCodecConfigurer();
    }

}
