package com.howtodoinjava.upgrade;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * Compiles the demo programs with --release 21 and runs them in a child JVM,
 * so the same class files run on JDK 21 and JDK 25.
 */
final class Jvm {

  static final Path JDK_25 = Path.of(System.getProperty("java.home"));
  static final Path JDK_21 = jdk21Home();

  record Result(int exitCode, String output) { }

  private Jvm() {
  }

  static Path compileDemosForJava21(Path outputDir) throws IOException {
    Path sources = Path.of("src/main/java/com/howtodoinjava/upgrade/demo");
    List<String> args = new ArrayList<>(List.of("--release", "21", "-nowarn", "-XDignore.symbol.file", "-d", outputDir.toString()));
    try (Stream<Path> files = Files.list(sources)) {
      files.map(Path::toString).forEach(args::add);
    }
    JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    int exit = javac.run(null, null, null, args.toArray(String[]::new));
    if (exit != 0) {
      throw new IllegalStateException("demo compilation failed");
    }
    return outputDir;
  }

  static Result run(Path jdkHome, Path classes, String mainClass, String... jvmOptions) throws Exception {
    List<String> command = new ArrayList<>();
    command.add(jdkHome.resolve("bin/java").toString());
    command.addAll(List.of(jvmOptions));
    command.addAll(List.of("-cp", classes.toString(), "com.howtodoinjava.upgrade.demo." + mainClass));
    ProcessBuilder builder = new ProcessBuilder(command).redirectErrorStream(true);
    builder.environment().remove("JAVA_TOOL_OPTIONS");
    Process process = builder.start();
    String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    return new Result(process.waitFor(), output);
  }

  static long elapsedMillis(String output) {
    return output.lines()
        .filter(line -> line.startsWith("elapsed ms = "))
        .mapToLong(line -> Long.parseLong(line.substring("elapsed ms = ".length()).trim()))
        .findFirst()
        .orElseThrow();
  }

  private static Path jdk21Home() {
    String home = System.getProperty("java21.home", "");
    if (home.isBlank() || home.startsWith("${")) {
      home = System.getenv().getOrDefault("JAVA21_HOME", "");
    }
    return home.isBlank() ? null : Path.of(home);
  }
}
