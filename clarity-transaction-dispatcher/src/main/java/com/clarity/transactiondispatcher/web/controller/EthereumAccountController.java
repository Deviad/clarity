package com.clarity.transactiondispatcher.web.controller;


import an.awesome.pipelinr.Pipeline;
import com.clarity.transactiondispatcher.services.Web3jService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class EthereumAccountController implements ClarityControllerMono {

    private final Pipeline queryPipeline;

    private final Pipeline commandPipeline;

    private final Web3jService web3;

    public EthereumAccountController(@Qualifier("queryPipelinr") Pipeline queryPipeline, @Qualifier("commandPipelinr") Pipeline commandPipeline, Web3jService web3) {
        this.queryPipeline = queryPipeline;
        this.commandPipeline = commandPipeline;
        this.web3 = web3;
    }

    public Mono<ServerResponse> createAccount(ServerRequest serverRequest) {
        Mono<ServerResponse> result;
        try {
            final Web3ClientVersion web3ClientVersion = web3.getWeb3().web3ClientVersion().sendAsync().get();
            result = getJsonSuccessResp(web3ClientVersion.getWeb3ClientVersion());
        } catch (Exception ex) {
            result = getJsonErrsResp(ex);
        }
        return result;
    }

}
