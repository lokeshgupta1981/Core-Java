package com.howtodoinjava.java26;

import java.net.http.HttpClient;
import java.util.Comparator;
import java.util.UUID;

/** Final Java 26 additions that need no flags. */
public class QuickLook {

  public static void main(String[] args) {
    HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_3)
        .build();
    UUID id = UUID.ofEpochMillis(System.currentTimeMillis());
    int version = id.version();
    String later = Comparator.<String>naturalOrder().max("apple", "banana");

    System.out.println("client version = " + client.version());
    System.out.println("uuid           = " + id + " (version " + version + ")");
    System.out.println("max            = " + later);
    client.close();
  }
}
