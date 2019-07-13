package com.clarity.transactiondispatcher;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;

@RequiredArgsConstructor
@Slf4j
public class AutoclosableWeb3 implements AutoCloseable {

    @Getter
    private final Web3j web3;

    @Override
    public void close()  {
      log.info("Web3j connection is closing...");
    }
}
