package com.howtodoinjava.java27;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** Starts a second JVM with extra options and returns its console output. */
final class ChildJvm {

  static String run(Map<String, String> env, String... args) throws IOException, InterruptedException {
    List<String> command = new ArrayList<>();
    command.add(Path.of(System.getProperty("java.home"), "bin", "java").toString());
    command.add("-cp");
    command.add(System.getProperty("java.class.path"));
    command.addAll(List.of(args));
    ProcessBuilder builder = new ProcessBuilder(command).redirectErrorStream(true);
    builder.environment().remove("JAVA_TOOL_OPTIONS");
    builder.environment().putAll(env);
    Process process = builder.start();
    String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    if (!process.waitFor(60, TimeUnit.SECONDS)) {
      process.destroyForcibly();
      throw new IllegalStateException("child JVM timed out");
    }
    return output;
  }

  static String run(String... args) throws IOException, InterruptedException {
    return run(Map.of(), args);
  }
}
