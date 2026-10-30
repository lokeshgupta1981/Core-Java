package com.howtodoinjava.classversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.spi.ToolProvider;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

/** Compiles the test fixtures with the current JDK and runs classes on other installed JDKs. */
final class Jdks {

  record Result(int exitCode, String output) {
  }

  private Jdks() {
  }

  /** Finds an installed JDK from JDK{N}_HOME or the usual Linux location; skips the test if missing. */
  static Path home(int feature) {
    Path home = Optional.ofNullable(System.getenv("JDK" + feature + "_HOME"))
        .map(Path::of)
        .orElse(Path.of("/usr/lib/jvm/java-" + feature + "-openjdk-amd64"));
    assumeTrue(Files.isExecutable(tool(home, "java")), "JDK " + feature + " not found at " + home);
    return home;
  }

  /** Path of a JDK tool such as java, javac or javap, with .exe on Windows. */
  static Path tool(Path jdkHome, String name) {
    boolean windows = System.getProperty("os.name").startsWith("Windows");
    return jdkHome.resolve("bin").resolve(windows ? name + ".exe" : name);
  }

  static Path fixture(String name) {
    try {
      return Path.of(Jdks.class.getResource("/fixtures/" + name).toURI());
    } catch (URISyntaxException e) {
      throw new IllegalStateException(e);
    }
  }

  /** Runs javac from the running JDK, like a build would. Returns exit code and diagnostics. */
  static Result javac(Path out, String fixtureDir, String... options) throws IOException {
    List<String> args = new ArrayList<>(List.of(options));
    args.addAll(List.of("-d", out.toString()));
    try (Stream<Path> files = Files.list(fixture(fixtureDir))) {
      files.map(Path::toString).forEach(args::add);
    }
    StringWriter diagnostics = new StringWriter();
    ToolProvider javac = ToolProvider.findFirst("javac").orElseThrow();
    int code = javac.run(new PrintWriter(diagnostics), new PrintWriter(diagnostics), args.toArray(String[]::new));
    return new Result(code, diagnostics.toString());
  }

  /** Starts a child JVM from the given JDK home. */
  static Result java(Path jdkHome, String... args) throws IOException, InterruptedException {
    List<String> command = new ArrayList<>();
    command.add(tool(jdkHome, "java").toString());
    command.addAll(List.of(args));
    ProcessBuilder builder = new ProcessBuilder(command).redirectErrorStream(true);
    builder.environment().remove("JAVA_TOOL_OPTIONS");
    Process process = builder.start();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    process.getInputStream().transferTo(output);
    int code = process.waitFor();
    return new Result(code, output.toString(StandardCharsets.UTF_8));
  }
}
