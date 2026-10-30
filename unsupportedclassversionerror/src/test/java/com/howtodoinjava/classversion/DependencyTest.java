package com.howtodoinjava.classversion;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyTest {

  @TempDir
  Path out;

  @Test
  void givenLibraryBuiltForJava25_whenAppRunsOnJava21_thenErrorAtFirstUse() throws Exception {
    Path jdk21 = Jdks.home(21);
    Path lib = out.resolve("lib");
    Path app = out.resolve("app");
    Jdks.javac(lib, "lib");
    Jdks.Result compile = Jdks.javac(app, "app", "--release", "21", "-cp", lib.toString());
    assertThat(compile.exitCode()).isZero();

    Jdks.Result run = Jdks.java(jdk21, "-cp", app + File.pathSeparator + lib, "com.howtodoinjava.Checkout");

    assertThat(run.output())
        .startsWith("Starting checkout")
        .contains("Exception in thread \"main\" java.lang.UnsupportedClassVersionError: "
            + "com/acme/pricing/TaxCalculator has been compiled by a more recent version of the Java Runtime "
            + "(class file version 69.0)")
        .contains("at com.howtodoinjava.Checkout.main(Checkout.java:8)");
  }

  @Test
  void givenLibraryBuiltForJava25_whenCompilingWithJdk21_thenBadClassFile() throws Exception {
    Path jdk21 = Jdks.home(21);
    Path lib = out.resolve("lib");
    Jdks.javac(lib, "lib");

    ProcessBuilder builder = new ProcessBuilder(Jdks.tool(jdk21, "javac").toString(),
        "-cp", lib.toString(), "-d", out.resolve("app").toString(),
        Jdks.fixture("app").resolve("Checkout.java").toString()).redirectErrorStream(true);
    builder.environment().remove("JAVA_TOOL_OPTIONS");
    String output = new String(builder.start().getInputStream().readAllBytes());

    assertThat(output)
        .contains("bad class file")
        .contains("class file has wrong version 69.0, should be 65.0");
  }
}
