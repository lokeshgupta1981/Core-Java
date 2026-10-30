package com.howtodoinjava.ids;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.f4b6a3.ulid.UlidCreator;
import com.github.f4b6a3.uuid.UuidCreator;

/**
 * Inserts rows with a UUID primary key into an H2 file database and prints the
 * insert time and the database file size for each kind of ID.
 *
 * Usage: java -cp ... com.howtodoinjava.ids.InsertBenchmark [rows] [runs]
 */
public class InsertBenchmark {

  private static final int BATCH = 1_000;

  public static void main(String[] args) throws Exception {
    int rows = args.length > 0 ? Integer.parseInt(args[0]) : 1_000_000;
    int runs = args.length > 1 ? Integer.parseInt(args[1]) : 3;

    Map<String, Supplier<UUID>> generators = new LinkedHashMap<>();
    generators.put("UUIDv4", UUID::randomUUID);
    generators.put("UUIDv7", UuidCreator::getTimeOrderedEpoch);
    generators.put("ULID", () -> UlidCreator.getMonotonicUlid().toUuid());

    for (Map.Entry<String, Supplier<UUID>> entry : generators.entrySet()) {
      insert(entry.getValue(), 100_000);            // warm-up, result ignored
    }

    System.out.printf("rows=%d runs=%d java=%s os=%s cpus=%d%n", rows, runs,
        System.getProperty("java.version"), System.getProperty("os.name"),
        Runtime.getRuntime().availableProcessors());
    for (Map.Entry<String, Supplier<UUID>> entry : generators.entrySet()) {
      long[] millis = new long[runs];
      long[] bytes = new long[runs];
      for (int r = 0; r < runs; r++) {
        long[] result = insert(entry.getValue(), rows);
        millis[r] = result[0];
        bytes[r] = result[1];
      }
      Arrays.sort(millis);
      Arrays.sort(bytes);
      System.out.printf("%-7s median %6d ms   file %6.1f MB   runs %s%n", entry.getKey(),
          millis[runs / 2], bytes[runs / 2] / 1_048_576.0, Arrays.toString(millis));
    }
  }

  private static long[] insert(Supplier<UUID> ids, int rows) throws SQLException, IOException {
    Path dir = Files.createTempDirectory("ids");
    String url = "jdbc:h2:file:" + dir.resolve("orders") + ";CACHE_SIZE=16384";
    long start;
    long elapsed;
    try (Connection con = DriverManager.getConnection(url, "sa", "")) {
      try (Statement st = con.createStatement()) {
        st.execute("CREATE TABLE orders (id UUID PRIMARY KEY, total INT NOT NULL)");
      }
      con.setAutoCommit(false);
      start = System.nanoTime();
      try (PreparedStatement ps = con.prepareStatement("INSERT INTO orders VALUES (?, ?)")) {
        for (int i = 1; i <= rows; i++) {
          ps.setObject(1, ids.get());
          ps.setInt(2, i);
          ps.addBatch();
          if (i % BATCH == 0) {
            ps.executeBatch();
            con.commit();
          }
        }
      }
      elapsed = (System.nanoTime() - start) / 1_000_000;
    }
    long size;
    try (Stream<Path> files = Files.walk(dir)) {
      size = files.filter(Files::isRegularFile).mapToLong(p -> p.toFile().length()).sum();
    }
    try (Stream<Path> files = Files.walk(dir)) {
      files.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
    }
    return new long[] {elapsed, size};
  }
}
