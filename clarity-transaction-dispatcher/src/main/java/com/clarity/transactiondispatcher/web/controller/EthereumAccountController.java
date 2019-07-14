package com.clarity.transactiondispatcher.web.controller;


import an.awesome.pipelinr.Pipeline;
import com.clarity.transactiondispatcher.services.Web3jService;
import com.clarity.transactiondispatcher.web.model.AccountRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Slf4j
public class EthereumAccountController implements ClarityControllerMono {

    private final Pipeline queryPipeline;

    private final Pipeline commandPipeline;

    private final Web3jService web3;


    public EthereumAccountController(
            @Qualifier("queryPipelinr") Pipeline queryPipeline,
            @Qualifier("commandPipelinr") Pipeline commandPipeline,
            Web3jService web3) {
        this.queryPipeline = queryPipeline;
        this.commandPipeline = commandPipeline;
        this.web3 = web3;
    }
    @PostMapping("/ethaccount")
    public Mono<Map<String, Object>> createAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        Mono<Map<String, Object>> result;

        //TODO: add global errorApi and use PipeLinr to keep the controller clean

        try {
            final Web3ClientVersion web3ClientVersion = web3.getWeb3().web3ClientVersion().sendAsync().get();
            String password = accountRequestDTO.getPassword();
            ECKeyPair keyPair = Keys.createEcKeyPair();
            WalletFile wallet = Wallet.createStandard(password, keyPair);
            Map<String, String> walletInfo = new LinkedHashMap<>();
            walletInfo.put("keyPair", keyPair.getPrivateKey().toString(16));
            walletInfo.put("address", wallet.getAddress());
            walletInfo.put("id", wallet.getId());

            result = getJsonSuccessResp(walletInfo);
        } catch (Exception ex) {
            result = getJsonErrsResp(ex);
        }
        return result;

    }

}
