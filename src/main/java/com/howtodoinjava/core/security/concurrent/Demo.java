package com.howtodoinjava.core.security.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class Demo {

  public static void main(String[] args) {

    // Define your secret key and salt (keep these secure and don't hardcode in production)
    String secretKey = "MySecretKey";
    String salt = "MySalt";

    Random rand = new Random();
    List<String> dataLines = Stream
        .generate(() -> "Token Value : " + rand.nextInt(100))
        .limit(100)
        .toList();

    int numberOfThreads = 4; // Adjust based on your system capabilities
    ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

    List<Future<String>> futures = new ArrayList<>();
    for (String dataLine : dataLines) {
      // Create new AES256 instance for each line or data chunk
      AES256EncryptionTask task = new AES256EncryptionTask(dataLine, secretKey, salt);
      futures.add(executor.submit(task));
    }

    // Wait for all tasks to complete and collect results
    for (Future<String> future : futures) {
      try {
        String encryptedData = future.get();
        System.out.println(encryptedData);
        // Process the encrypted data, e.g., write to file
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    executor.shutdown();
  }
}
