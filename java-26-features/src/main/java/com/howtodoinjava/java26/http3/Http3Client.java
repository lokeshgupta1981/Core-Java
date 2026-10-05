package com.howtodoinjava.java26.http3;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/** JEP 517: opt in to HTTP/3. The client falls back to HTTP/2 or HTTP/1.1 when HTTP/3 is not possible. */
public class Http3Client {

  public static HttpClient newClient() {
    return HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_3)
        .connectTimeout(Duration.ofSeconds(5))
        .build();
  }

  public static HttpResponse<String> get(HttpClient client, String url)
      throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder(URI.create(url))
        .timeout(Duration.ofSeconds(10))
        .GET()
        .build();
    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  public static void main(String[] args) throws Exception {
    String url = args.length > 0 ? args[0] : "https://openjdk.org/";
    try (HttpClient client = newClient()) {
      HttpResponse<String> response = get(client, url);
      System.out.println(response.statusCode() + " " + response.version());
    }
  }
}
