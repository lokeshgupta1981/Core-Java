package com.howtodoinjava.java26;

import static org.assertj.core.api.Assertions.assertThat;

import com.howtodoinjava.java26.http3.Http3Client;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

class Http3ClientTest {

  @Test
  void fallsBackWhenTheServerHasNoHttp3() throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext("/", exchange -> {
      byte[] body = "apple".getBytes();
      exchange.sendResponseHeaders(200, body.length);
      exchange.getResponseBody().write(body);
      exchange.close();
    });
    server.start();
    try (HttpClient client = Http3Client.newClient()) {
      HttpResponse<String> response = Http3Client.get(client, "http://127.0.0.1:" + server.getAddress().getPort() + "/");
      assertThat(client.version()).isEqualTo(HttpClient.Version.HTTP_3);
      assertThat(response.body()).isEqualTo("apple");
      assertThat(response.version()).isEqualTo(HttpClient.Version.HTTP_1_1);
    } finally {
      server.stop(0);
    }
  }
}
