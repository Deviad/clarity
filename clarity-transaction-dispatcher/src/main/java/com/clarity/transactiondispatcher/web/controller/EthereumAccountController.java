package com.clarity.transactiondispatcher.web.controller;


import an.awesome.pipelinr.Pipeline;
import com.clarity.transactiondispatcher.services.Web3jService;
import com.clarity.transactiondispatcher.web.model.AccountRequestDTO;
import com.clarity.transactiondispatcher.web.validation.RequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class EthereumAccountController implements ClarityControllerMono {

    private final Pipeline queryPipeline;

    private final Pipeline commandPipeline;

    private final Web3jService web3;

    private final RequestHandler requestHandler;

    public EthereumAccountController(@Qualifier("queryPipelinr") Pipeline queryPipeline, @Qualifier("commandPipelinr") Pipeline commandPipeline, Web3jService web3, RequestHandler requestHandler) {
        this.queryPipeline = queryPipeline;
        this.commandPipeline = commandPipeline;
        this.web3 = web3;
        this.requestHandler = requestHandler;
    }

    public Mono<ServerResponse> createAccount(ServerRequest serverRequest) {
//        Mono<ServerResponse> result;
//        final AccountRequestDTO accountRequestDTO = serverRequest.bodyToMono(AccountRequestDTO.class).block();
//        try {
////            final Web3ClientVersion web3ClientVersion = web3.getWeb3().web3ClientVersion().sendAsync().get();
//            String password = accountRequestDTO.getPassword();
//            ECKeyPair keyPair = Keys.createEcKeyPair();
//            WalletFile wallet = Wallet.createStandard(password, keyPair);
//            Map<String, String> walletInfo = new LinkedHashMap<>();
//            walletInfo.put("keyPair", keyPair.getPrivateKey().toString(16));
//            walletInfo.put("address", wallet.getAddress());
//            walletInfo.put("id", wallet.getId());
//
//            result = getJsonSuccessResp(walletInfo);
//        } catch (Exception ex) {
//            result = getJsonErrsResp(ex);
//        }
//        return result;

        return requestHandler.requireValidBody(body -> getJsonErrsResp("testtttt"), serverRequest, AccountRequestDTO.class);


    }

}
