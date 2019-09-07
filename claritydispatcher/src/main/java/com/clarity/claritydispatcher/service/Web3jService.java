package com.clarity.claritydispatcher.service;

import io.micronaut.context.annotation.Value;
import lombok.Data;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
@Data
public class Web3jService {

  @Value("${infura.https-endpoint}")
  private String infuraHttpsEndpoint;

  @Value("${infura.ws-endpoint}")
  private String infuraWsEndpoint;

  private Web3j web3;

  @PostConstruct
  public void init() {
    System.out.println("testtt " + infuraHttpsEndpoint);
    web3 = Web3j.build(new HttpService(infuraHttpsEndpoint));
  }
}
