package com.howtodoinjava.java27;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JfrRedactionTest {

  @TempDir
  Path dir;

  @Test
  void secretsAreRedactedInTheRecording() throws Exception {
    Path recording = dir.resolve("shop.jfr");
    String output = ChildJvm.run(Map.of("DB_TOKEN", "abc123"),
        "-XX:StartFlightRecording:filename=" + recording,
        "-Ddb.password=secret123", "-Dapp.name=shop",
        "com.howtodoinjava.java27.ShopApp", "--password", "secret456", "--verbose");
    assertThat(output).contains("Shop started with 3 arguments");

    List<RecordedEvent> events = RecordingFile.readAllEvents(recording);
    Map<String, String> values = new HashMap<>();
    for (RecordedEvent e : events) {
      String type = e.getEventType().getName();
      if (type.equals("jdk.InitialSystemProperty") || type.equals("jdk.InitialEnvironmentVariable")) {
        values.put(e.getString("key"), e.getString("value"));
      }
      if (type.equals("jdk.JVMInformation")) {
        values.put("javaArguments", e.getString("javaArguments"));
      }
    }
    assertThat(values)
        .containsEntry("db.password", "[REDACTED]")
        .containsEntry("DB_TOKEN", "[REDACTED]")
        .containsEntry("app.name", "shop")
        .containsEntry("javaArguments",
            "com.howtodoinjava.java27.ShopApp [REDACTED] [REDACTED] --verbose");
    assertThat(Files.readString(recording, java.nio.charset.StandardCharsets.ISO_8859_1))
        .doesNotContain("secret123", "secret456", "abc123");
  }
}
