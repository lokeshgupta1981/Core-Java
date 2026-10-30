package com.howtodoinjava.classversion;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class ReleaseVsTargetTest {

  @TempDir
  Path out;

  @Test
  void givenSourceAndTarget8_whenUsingJava9Api_thenCompilesButFailsOnJava8() throws Exception {
    Path jdk8 = Jdks.home(8);
    Jdks.Result compile = Jdks.javac(out, "genres", "-source", "8", "-target", "8");

    assertThat(compile.exitCode()).isZero();
    assertThat(compile.output()).contains("--release 8 is recommended instead of -source 8 -target 8");
    assertThat(ClassFileVersion.of(out.resolve("com/howtodoinjava/Genres.class")).major()).isEqualTo(52);

    Jdks.Result run = Jdks.java(jdk8, "-cp", out.toString(), "com.howtodoinjava.Genres");

    assertThat(run.output()).contains("java.lang.NoSuchMethodError: java.util.List.of(");
  }

  @Test
  void givenRelease8_whenUsingJava9Api_thenCompilationFails() throws Exception {
    Jdks.Result compile = Jdks.javac(out, "genres", "--release", "8");

    assertThat(compile.exitCode()).isNotZero();
    assertThat(compile.output())
        .contains("error: cannot find symbol")
        .contains("symbol:   method of(String,String)")
        .contains("location: interface List");
  }
}
