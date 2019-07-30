package com.clarity.transactiondispatcher.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

@Component
@Slf4j
public class EthereumClient {

 private final Web3jService web3jService;
 private final NettyWsRequestFactory client;

    public EthereumClient(NettyWsRequestFactory client, Web3jService web3jService) {
        this.web3jService = web3jService;
        this.client = client;
    }


    public Flux<String> request(final Map<String, Object> reqParams) {
        return client.request(web3jService.getInfuraWsEndpoint(), reqParams);
    }

}
