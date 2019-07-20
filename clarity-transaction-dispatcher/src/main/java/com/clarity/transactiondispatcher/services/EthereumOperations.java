package com.clarity.transactiondispatcher.services;

import com.clarity.transactiondispatcher.utils.JSONAble;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.util.Base64;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSubscribe;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.protocol.websocket.events.NewHeadsNotification;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class EthereumOperations implements JSONAble {


    private final Web3jService web3jService;

    public EthereumOperations(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    public Map<String, Object> sendTransaction(String password, WalletFile wallet, BigDecimal amount, String toAddress) {
        Map<String, Object> result = new HashMap<>();
        try {
            //Reminder: you can get credentials also like this:
//            final Credentials FOOBAR = Credentials.create(keyPair);

            Credentials credentials = Credentials.create(Wallet.decrypt(password, wallet));
            // Address must have a shape like 0x31B98D14007bDEe637298086988A0bBd31184523
            TransactionReceipt receipt = Transfer.sendFunds(web3jService.getWeb3(), credentials, "0x" + toAddress, amount, Convert.Unit.ETHER).sendAsync().join();

            result.put("transactionHash", receipt.getTransactionHash());
            result.put("transactionStatus", receipt.getStatus());

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;
    }

    @SneakyThrows
    public BigInteger getBalance(WalletFile wallet) {
        return web3jService.getWeb3()
                .ethGetBalance("0x" + wallet.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync()
                .join().getBalance();
    }

    @SneakyThrows
    public BigInteger getUpdatedBalance(WalletFile wallet) {

        return new BigInteger("10");

    }

    public BigInteger getNonce(String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount = web3jService.getWeb3().ethGetTransactionCount(
                address, DefaultBlockParameterName.LATEST).sendAsync().get();
        return ethGetTransactionCount.getTransactionCount();
    }

    @SneakyThrows
    public Map<String, String> createAccount(String password) {
        ECKeyPair keyPair = Keys.createEcKeyPair();
        WalletFile wallet = Wallet.createStandard(password, keyPair);
        Map<String, String> walletInfo = new LinkedHashMap<>();
        walletInfo.put("keyPair", keyPair.getPrivateKey().toString(16));
        walletInfo.put("address", wallet.getAddress());
        walletInfo.put("wallet", Base64.encodeBytes(toJSON(wallet).getBytes()));
        return walletInfo;
    }
}
