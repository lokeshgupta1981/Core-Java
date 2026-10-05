package com.howtodoinjava.classversion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

/**
 * Starts Hello (major version 69) on an older JDK. Set the JDK21_HOME environment
 * variable to a JDK 21 installation to run this test; otherwise it is skipped.
 */
class OlderRuntimeTest {

  private static final Path CLASSES = Path.of(System.getProperty("classes.dir", "target/classes"));

  @Test
  void jdk21RefusesJava25ClassFile() throws IOException, InterruptedException {
    String jdk21Home = System.getenv("JDK21_HOME");
    assumeTrue(jdk21Home != null && !jdk21Home.isBlank(), "JDK21_HOME is not set");
    Path java = Path.of(jdk21Home, "bin", "java");
    assumeTrue(Files.isExecutable(java), "No java executable in JDK21_HOME");

    ProcessBuilder builder = new ProcessBuilder(java.toString(), "-cp", CLASSES.toString(),
        "com.howtodoinjava.classversion.Hello");
    builder.redirectErrorStream(true);
    builder.environment().remove("JAVA_TOOL_OPTIONS");
    Process process = builder.start();

    boolean finished = process.waitFor(30, TimeUnit.SECONDS);
    if (!finished) {
      process.destroyForcibly();
    }
    String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

    assertThat(finished).isTrue();
    assertThat(process.exitValue()).isEqualTo(1);
    assertThat(output)
        .contains("java.lang.UnsupportedClassVersionError")
        .contains("class file version 69.0")
        .contains("only recognizes class file versions up to 65.0");
  }
}
