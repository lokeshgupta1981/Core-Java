package com.howtodoinjava.pqc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 * Starts a TLS 1.3 server and client in the same JVM, both trusting one self-signed certificate.
 */
public class LocalTls {

  private final SSLContext context;

  public LocalTls(Path keyStore, char[] password) throws GeneralSecurityException, IOException {
    KeyStore store = KeyStore.getInstance("PKCS12");
    try (InputStream in = Files.newInputStream(keyStore)) {
      store.load(in, password);
    }
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
    kmf.init(store, password);
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
    tmf.init(store);

    context = SSLContext.getInstance("TLSv1.3");
    context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
  }

  public SSLContext context() {
    return context;
  }

  /**
   * Runs one handshake. A null group list keeps the JDK defaults for that side.
   * Returns the negotiated protocol, or throws the client's handshake failure.
   */
  public String handshake(String[] serverGroups, String[] clientGroups) throws Exception {
    try (SSLServerSocket server = (SSLServerSocket) context.getServerSocketFactory().createServerSocket(0);
         ExecutorService executor = Executors.newSingleThreadExecutor()) {
      if (serverGroups != null) {
        SSLParameters params = server.getSSLParameters();
        params.setNamedGroups(serverGroups);
        server.setSSLParameters(params);
      }
      Future<?> accepted = executor.submit(() -> {
        try (SSLSocket socket = (SSLSocket) server.accept()) {
          socket.startHandshake();
        }
        return null;
      });

      try (SSLSocket client = (SSLSocket) context.getSocketFactory().createSocket("localhost", server.getLocalPort())) {
        if (clientGroups != null) {
          SSLParameters params = client.getSSLParameters();
          params.setNamedGroups(clientGroups);
          client.setSSLParameters(params);
        }
        client.startHandshake();
        return client.getSession().getProtocol();
      } finally {
        try {
          accepted.get(5, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // the client side reports the failure
        }
      }
    }
  }
}
