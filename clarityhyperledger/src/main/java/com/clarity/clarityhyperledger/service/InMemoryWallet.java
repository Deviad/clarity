/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.clarity.clarityhyperledger.service;

import io.micronaut.runtime.http.scope.RequestScope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequestScope
public class InMemoryWallet implements Wallet {
  private Map<String, Identity> store = new HashMap<>();

  @Override
  public void put(String label, Identity identity) {
    store.put(label, identity);
  }

  @Override
  public Identity get(String label) {
    return store.get(label);
  }

  @Override
  public Set<String> getAllLabels() {
    return store.keySet();
  }

  @Override
  public void remove(String label) {
    store.remove(label);
  }

  @Override
  public boolean exists(String label) {
    return store.containsKey(label);
  }

}
