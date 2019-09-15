package com.clarity.clarityhyperledger.service;

import java.security.PrivateKey;

public interface Enrollment {
    String getCertificate();
    PrivateKey getPrivateKey();
}
