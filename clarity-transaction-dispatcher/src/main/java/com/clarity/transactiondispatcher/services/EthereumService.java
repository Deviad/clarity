package com.clarity.transactiondispatcher.services;

import com.clarity.transactiondispatcher.utils.JSONAble;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.util.Base64;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class EthereumService implements JSONAble {

    private final Web3jService web3jService;

    public EthereumService(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    @SneakyThrows
    public Map<String, Object> sendTransaction(String password, WalletFile wallet, BigDecimal amount,
            String toAddress) {
        Map<String, Object> result = new HashMap<>();
        // Reminder: you can get credentials also like this:
        // final Credentials FOOBAR = Credentials.create(keyPair);

        Credentials credentials = Credentials.create(Wallet.decrypt(password, wallet));
        // Address must have a shape like 0x31B98D14007bDEe637298086988A0bBd31184523
        TransactionReceipt receipt = Transfer
                .sendFunds(web3jService.getWeb3(), credentials, "0x" + toAddress, amount, Convert.Unit.ETHER)
                .sendAsync().join();

        result.put("transactionHash", receipt.getTransactionHash());
        result.put("transactionStatus", receipt.getStatus());

        return result;
    }

    @SneakyThrows
    public BigInteger getBalance(WalletFile wallet) {
        return web3jService.getWeb3().ethGetBalance("0x" + wallet.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync().join().getBalance();
    }

    @SneakyThrows
    public BigInteger getUpdatedBalance(WalletFile wallet) {

        return new BigInteger("10");

    }

    public BigInteger getNonce(String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount = web3jService.getWeb3()
                .ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
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
        log.info("wallet info -- {}", walletInfo);
        return walletInfo;
    }
}
