/*
 * SPDX-License-Identifier: Apache2
 */

package com.clarity.claritychaincode;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class Coin {

    @Property()
    private String value;

    public Coin(){
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    public static Coin fromJSONString(String json) {
        String value = new JSONObject(json).getString("value");
        Coin asset = new Coin();
        asset.setValue(value);
        return asset;
    }
}
