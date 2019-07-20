package com.clarity.transactiondispatcher.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Component
public class Web3jService implements InitializingBean {

    @Value("${infura.https-endpoint}")
    @Getter
    private String infuraHttpsEndpoint;

    @Value("${infura.ws-endpoint}")
    @Getter
    private String infuraWsEndpoint;

    @Getter
    @Setter
    private Web3j web3;

    @Override
    public void afterPropertiesSet() {
        web3 = Web3j.build(new HttpService(infuraHttpsEndpoint));
    }
}
