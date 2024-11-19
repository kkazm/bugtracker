package ovh.kkazm.bugtracker;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CryptoTest {

    @Test
    void jwtHmac() {
        // Generate random 256-bit (32-byte) shared secret
        SecureRandom random = new SecureRandom();
        byte[] sharedSecret = new byte[64];
        random.nextBytes(sharedSecret);

        // Create HMAC signer
        JWSSigner signer = null;
        try {
            signer = new MACSigner(sharedSecret);
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        }

        // Prepare JWS object with "Hello, world!" payload
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS512), new Payload("Hello, world!"));

        // Apply the HMAC
        try {
            jwsObject.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        // To serialize to compact form, produces something like
        // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
        String s = jwsObject.serialize();

        // To parse the JWS and verify it, e.g. on client-side
        try {
            jwsObject = JWSObject.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JWSVerifier verifier = null;
        try {
            verifier = new MACVerifier(sharedSecret);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        try {
            assertTrue(jwsObject.verify(verifier));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        assertEquals("Hello, world!", jwsObject.getPayload().toString());
    }

    @Test
    void rsa() throws JOSEException, ParseException {
        // RSA signatures require a public and private RSA key pair,
        // the public key must be made known to the JWS recipient to
        // allow the signatures to be verified
        RSAKey rsaJWK = new RSAKeyGenerator(2048)
                .keyID("123")
                .generate();
        RSAKey rsaPublicJWK = rsaJWK.toPublicJWK();

        // Create RSA-signer with the private key
        JWSSigner signer = new RSASSASigner(rsaJWK);

        // Prepare JWS object with simple string as payload
        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                new Payload("In RSA we trust!"));

        // Compute the RSA signature
        jwsObject.sign(signer);

        // To serialize to compact form, produces something like
        // eyJhbGciOiJSUzI1NiJ9.SW4gUlNBIHdlIHRydXN0IQ.IRMQENi4nJyp4er2L
        // mZq3ivwoAjqa1uUkSBKFIX7ATndFF5ivnt-m8uApHO4kfIFOrW7w2Ezmlg3Qd
        // maXlS9DhN0nUk_hGI3amEjkKd0BWYCB8vfUbUv0XGjQip78AI4z1PrFRNidm7
        // -jPDm5Iq0SZnjKjCNS5Q15fokXZc8u0A
        String s = jwsObject.serialize();

        // To parse the JWS and verify it, e.g. on client-side
        jwsObject = JWSObject.parse(s);

        JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);

        assertTrue(jwsObject.verify(verifier));

        assertEquals("In RSA we trust!", jwsObject.getPayload().toString());
    }

}
