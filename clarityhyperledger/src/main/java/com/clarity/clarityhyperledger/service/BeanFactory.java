package com.clarity.clarityhyperledger.service;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import lombok.SneakyThrows;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcECContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.inject.Singleton;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Factory
public class BeanFactory {
    @Bean
    @Singleton
    public Provider securityProviderFactory() {
        return new BouncyCastleProvider();
    }

    @SneakyThrows
    @Bean
    @Singleton
    public Map<String, Object> pemKeyFactory() {
        Security.addProvider(securityProviderFactory());
        // GENERATE THE PUBLIC/PRIVATE RSA KEY PAIR
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC", "BC");
        generator.initialize(384);
        KeyPair keyPair = generator.generateKeyPair();
        // GENERATE THE X509 CERTIFICATE
        X500Name dnName = new X500Name("CN=John Doe");
        // yesterday
        Date validityBeginDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        // in 2 years
        Date validityEndDate = new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000);
        SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        X509v1CertificateBuilder builder = new X509v1CertificateBuilder(
                dnName,
                BigInteger.valueOf(System.currentTimeMillis()),
                validityBeginDate,
                validityEndDate,
                Locale.getDefault(),
                dnName,
                subPubKeyInfo);

        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256WithRSAEncryption");
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
        ContentSigner contentSigner = new BcECContentSignerBuilder(sigAlgId, digAlgId)
                .build(PrivateKeyFactory.createKey(keyPair.getPrivate().getEncoded()));

        X509CertificateHolder holder = builder.build(contentSigner);

        X509Certificate cert = new JcaX509CertificateConverter().getCertificate(holder);

        StringWriter string = new StringWriter();
        try(PemWriter pemWriter = new PemWriter(string)) {
            PemObject pemObject = new PemObject("CERTIFICATE", cert.getEncoded());
            pemWriter.writeObject(pemObject);
        }
        String certificate = string.toString();
        PrivateKey pk = keyPair.getPrivate();
        Map<String, Object> certPkPair = new LinkedHashMap<>();
        certPkPair.put("certificate", certificate);
        certPkPair.put("pk", pk);
        return certPkPair;

    }
}
