package com.clarity.transactiondispatcher.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;

@Configuration
public class ServerConfiguration {

    @Bean
    DefaultServerCodecConfigurer defaultServerCodecConfigurer() {
        return new DefaultServerCodecConfigurer();
    }

}
