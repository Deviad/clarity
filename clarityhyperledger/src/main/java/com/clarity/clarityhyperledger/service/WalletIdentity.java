/*
 * Copyright 2019 IBM All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.clarity.clarityhyperledger.service;

import java.security.PrivateKey;

public final class WalletIdentity implements Wallet.Identity {
  private final String mspId;
  private final String certificate;
  private final PrivateKey privateKey;

  public WalletIdentity(String mspId, String certificate, PrivateKey privateKey) {
    this.mspId = mspId;
    this.certificate = certificate;
    this.privateKey = privateKey;
  }

  @Override
  public String getMspId() {
    return this.mspId;
  }

  @Override
  public String getCertificate() {
    return this.certificate;
  }

  @Override
  public PrivateKey getPrivateKey() {
    return this.privateKey;
  }

}
