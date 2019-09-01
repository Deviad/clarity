/*
 * SPDX-License-Identifier: Apache2
 */
package com.clarity.claritychaincode;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import static java.nio.charset.StandardCharsets.UTF_8;

@Contract(name = "CoinContract",
    info = @Info(title = "Coin contract",
                description = "It defines the interactions amongst the entities of the domain of reference",
                version = "0.0.1",
                license =
                        @License(name = "Apache2",
                                url = ""),
                                contact =  @Contact(email = "clarity-chaincode@example.com",
                                                name = "clarity-chaincode",
                                                url = "http://clarity-chaincode.me")))
@Default
public class CoinContract implements ContractInterface {
    public  CoinContract() {

    }
    @Transaction()
    public boolean coinExists(Context ctx, String coinId) {
        byte[] buffer = ctx.getStub().getState(coinId);
        return (buffer != null && buffer.length > 0);
    }

    @Transaction()
    public void createCoin(Context ctx, String coinId, String value) {
        boolean exists = coinExists(ctx,coinId);
        if (exists) {
            throw new RuntimeException("The asset "+coinId+" already exists");
        }
        Coin asset = new Coin();
        asset.setValue(value);
        ctx.getStub().putState(coinId, asset.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public Coin readCoin(Context ctx, String coinId) {
        boolean exists = coinExists(ctx,coinId);
        if (!exists) {
            throw new RuntimeException("The asset "+coinId+" does not exist");
        }

        Coin newAsset = Coin.fromJSONString(new String(ctx.getStub().getState(coinId),UTF_8));
        return newAsset;
    }

    @Transaction()
    public void updateCoin(Context ctx, String coinId, String newValue) {
        boolean exists = coinExists(ctx,coinId);
        if (!exists) {
            throw new RuntimeException("The asset "+coinId+" does not exist");
        }
        Coin asset = new Coin();
        asset.setValue(newValue);

        ctx.getStub().putState(coinId, asset.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public void deleteCoin(Context ctx, String coinId) {
        boolean exists = coinExists(ctx,coinId);
        if (!exists) {
            throw new RuntimeException("The asset "+coinId+" does not exist");
        }
        ctx.getStub().delState(coinId);
    }

}
