package com.howtodoinjava.core.security;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;

/**
 * Examples for the tutorial "Java EdDSA Example: Sign and Verify With Ed25519 and Ed448".
 * https://howtodoinjava.com/java15/java-eddsa-example/
 */
public class EddsaSignVerifyFlow {
    static byte[] sign(PrivateKey key, String message) throws GeneralSecurityException {
        Signature signature = Signature.getInstance("Ed25519");
        signature.initSign(key);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }
    static boolean verify(PublicKey key, String message, byte[] sig) throws GeneralSecurityException {
        Signature signature = Signature.getInstance("Ed25519");
        signature.initVerify(key);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return signature.verify(sig);
    }
    static String issueLicense(PrivateKey key, String content) throws GeneralSecurityException {
        String sig = Base64.getEncoder().encodeToString(sign(key, content));
        return content + "\nsignature=" + sig;
    }
    static boolean isLicenseValid(PublicKey key, String file) throws GeneralSecurityException {
        int split = file.lastIndexOf("\nsignature=");
        if (split < 0) {
            return false;
        }
        String content = file.substring(0, split);
        byte[] sig = Base64.getDecoder().decode(file.substring(split + 11));
        return verify(key, content, sig);
    }
    public static void main(String[] args) throws Exception {
        {
            KeyPair keyPair = KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
            byte[] message = "order 42 paid".getBytes(StandardCharsets.UTF_8);

            Signature signer = Signature.getInstance("Ed25519");
            signer.initSign(keyPair.getPrivate());
            signer.update(message);
            byte[] signature = signer.sign();

            Signature verifier = Signature.getInstance("Ed25519");
            verifier.initVerify(keyPair.getPublic());
            verifier.update(message);
            boolean valid = verifier.verify(signature);   // true
            show("valid", valid);
            int signatureBytes = signature.length;        // 64
            show("signatureBytes", signatureBytes);
        }
        {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("Ed25519");
            KeyPair pair = generator.generateKeyPair();

            String algorithm = pair.getPublic().getAlgorithm();      // "EdDSA"
            show("algorithm", algorithm);
            String publicFormat = pair.getPublic().getFormat();      // "X.509"
            show("publicFormat", publicFormat);
            String privateFormat = pair.getPrivate().getFormat();    // "PKCS#8"
            show("privateFormat", privateFormat);
            int publicSize = pair.getPublic().getEncoded().length;   // 44
            show("publicSize", publicSize);
            int privateSize = pair.getPrivate().getEncoded().length; // 48
            show("privateSize", privateSize);
        }
        {
            KeyPairGenerator sized = KeyPairGenerator.getInstance("Ed25519");
            try { sized.initialize(256);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            KeyPair keys = KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
            byte[] sig = sign(keys.getPrivate(), "refund 15.00 EUR");

            boolean original = verify(keys.getPublic(), "refund 15.00 EUR", sig);    // true
            show("original", original);
            boolean tampered = verify(keys.getPublic(), "refund 95.00 EUR", sig);    // false
            show("tampered", tampered);
            boolean same = Arrays.equals(sig, sign(keys.getPrivate(), "refund 15.00 EUR"));   // true
            show("same", same);
        }
        {
            KeyPair original = KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
            String publicText = Base64.getEncoder().encodeToString(original.getPublic().getEncoded());
            String privateText = Base64.getEncoder().encodeToString(original.getPrivate().getEncoded());

            KeyFactory factory = KeyFactory.getInstance("Ed25519");
            PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicText)));
            PrivateKey privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateText)));

            boolean restored = publicKey.equals(original.getPublic());   // true
            show("restored", restored);
            int publicTextLength = publicText.length();                  // 60
            show("publicTextLength", publicTextLength);
        }
        {
            KeyPair ed448 = KeyPairGenerator.getInstance("Ed448").generateKeyPair();
            Signature ed448Signer = Signature.getInstance("Ed448");
            ed448Signer.initSign(ed448.getPrivate());
            ed448Signer.update("firmware 2.1".getBytes(StandardCharsets.UTF_8));
            int ed448Bytes = ed448Signer.sign().length;     // 114
            show("ed448Bytes", ed448Bytes);
        }
        {
            KeyPairGenerator generic = KeyPairGenerator.getInstance("EdDSA");
            generic.initialize(NamedParameterSpec.ED448);
            KeyPair genericPair = generic.generateKeyPair();
            String curve = ((EdECKey) genericPair.getPublic()).getParams().getName();   // "Ed448"
            show("curve", curve);

            Signature strict = Signature.getInstance("Ed25519");
            try { strict.initVerify(genericPair.getPublic());  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            KeyPair vendor = KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
            String license = issueLicense(vendor.getPrivate(), "customer=Ana\nexpires=2027-10-10");

            boolean genuine = isLicenseValid(vendor.getPublic(), license);                                       // true
            show("genuine", genuine);
            boolean edited = isLicenseValid(vendor.getPublic(), license.replace("2027", "2099"));               // false
            show("edited", edited);
            boolean unsigned = isLicenseValid(vendor.getPublic(), "customer=Ana\nexpires=2099-01-01");          // false
            show("unsigned", unsigned);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
