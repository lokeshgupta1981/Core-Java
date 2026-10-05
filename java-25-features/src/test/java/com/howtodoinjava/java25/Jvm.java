package com.howtodoinjava.java25;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Starts a child JVM (or a JDK tool) and returns its combined output. */
public final class Jvm {

  public record Result(int exitCode, String output) {
  }

  private Jvm() {
  }

  public static Path tool(String name) {
    return Path.of(System.getProperty("java.home"), "bin", name);
  }

  public static Result java(List<String> args) throws IOException, InterruptedException {
    return run(tool("java"), args, null);
  }

  public static Result run(Path tool, List<String> args, String stdin) throws IOException, InterruptedException {
    List<String> command = new ArrayList<>();
    command.add(tool.toString());
    command.addAll(args);
    ProcessBuilder pb = new ProcessBuilder(command).redirectErrorStream(true);
    pb.environment().remove("JAVA_TOOL_OPTIONS");
    pb.environment().remove("JDK_JAVA_OPTIONS");
    Process process = pb.start();
    if (stdin != null) {
      process.getOutputStream().write(stdin.getBytes(StandardCharsets.UTF_8));
    }
    process.getOutputStream().close();
    String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    if (!process.waitFor(2, TimeUnit.MINUTES)) {
      process.destroyForcibly();
      throw new IllegalStateException("timed out: " + command);
    }
    return new Result(process.exitValue(), output);
  }

  public static String classes() {
    return Path.of("target", "classes").toString();
  }
}
