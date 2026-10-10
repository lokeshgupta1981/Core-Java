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
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.net.URI;
import java.net.http.*;

/**
 * Examples for the tutorial "Bypass SSL Certificate Checking in Java (and Safer Fixes)".
 * https://howtodoinjava.com/java/java-security/bypass-ssl-certificate-checking-java/
 */
public class SslCertificateCheckFixes {
    // src/test/java: TEST ONLY
    static SSLContext insecureContext() throws GeneralSecurityException {
        TrustManager[] trustAll = { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
        }};
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, trustAll, new SecureRandom());
        return context;
    }
    static SSLContext trustOnly(Path pemFile) throws Exception {
        Certificate cert;
        try (InputStream in = Files.newInputStream(pemFile)) {
            cert = CertificateFactory.getInstance("X.509").generateCertificate(in);
        }
        KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
        store.load(null, null);                       // empty keystore
        store.setCertificateEntry("recipe-api", cert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(store);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        return context;
    }
    public static void main(String[] args) throws Exception {
        {
            // TEST ONLY: this client also accepts an attacker's certificate
            TrustManager[] trustAll = { new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }};
            SSLContext insecure = SSLContext.getInstance("TLS");
            insecure.init(null, trustAll, new SecureRandom());

            HttpClient client = HttpClient.newBuilder().sslContext(insecure).build();
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://localhost:8443/recipes")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();             // 200
            show("status", status);
        }
        {
            HttpClient defaultClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://localhost:8443/recipes")).build();
            try { HttpResponse<String> failed = defaultClient.send(request, HttpResponse.BodyHandlers.ofString()); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
        }
        {
            HttpRequest otherHost = HttpRequest.newBuilder(URI.create("https://localhost:8444/recipes")).build();
            HttpClient trustAllClient = HttpClient.newBuilder().sslContext(insecureContext()).build();
            try { HttpResponse<String> mismatch = trustAllClient.send(otherHost, HttpResponse.BodyHandlers.ofString()); show("mismatch", mismatch); } catch (Throwable _t) { System.out.println("mismatch -> " + _t); }
        }
        {
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            try (InputStream in = Files.newInputStream(Path.of("truststore.p12"))) {
                trustStore.load(in, "changeit".toCharArray());
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            SSLContext trusted = SSLContext.getInstance("TLS");
            trusted.init(null, tmf.getTrustManagers(), null);

            HttpClient client = HttpClient.newBuilder().sslContext(trusted).build();
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://localhost:8443/recipes")).build();
            String body = client.send(request, HttpResponse.BodyHandlers.ofString()).body();   // ["pancakes","omelette"]
            show("body", body);
        }
        {
            HttpClient pinned = HttpClient.newBuilder().sslContext(trustOnly(Path.of("server.crt"))).build();
            HttpRequest ours = HttpRequest.newBuilder(URI.create("https://localhost:8443/recipes")).build();
            HttpRequest other = HttpRequest.newBuilder(URI.create("https://localhost:8444/recipes")).build();
            int okStatus = pinned.send(ours, HttpResponse.BodyHandlers.ofString()).statusCode();      // 200
            show("okStatus", okStatus);
            try { int otherStatus = pinned.send(other, HttpResponse.BodyHandlers.ofString()).statusCode(); show("otherStatus", otherStatus); } catch (Throwable _t) { System.out.println("otherStatus -> " + _t); }
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
