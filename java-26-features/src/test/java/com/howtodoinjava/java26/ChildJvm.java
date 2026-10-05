package com.howtodoinjava.java26;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Starts a child JVM with extra flags and returns its combined output. */
public final class ChildJvm {

  private ChildJvm() {
  }

  public static String run(List<String> flags, String mainClass) throws IOException, InterruptedException {
    return run(flags, System.getProperty("java.class.path"), mainClass);
  }

  public static String run(List<String> flags, String classPath, String mainClass)
      throws IOException, InterruptedException {
    List<String> command = new ArrayList<>();
    command.add(Path.of(System.getProperty("java.home"), "bin", "java").toString());
    command.addAll(flags);
    command.add("-cp");
    command.add(classPath);
    command.add(mainClass);
    ProcessBuilder builder = new ProcessBuilder(command).redirectErrorStream(true);
    builder.environment().remove("JAVA_TOOL_OPTIONS");
    try (Process process = builder.start()) {
      String output = new String(process.getInputStream().readAllBytes());
      process.waitFor();
      return output;
    }
  }
}
