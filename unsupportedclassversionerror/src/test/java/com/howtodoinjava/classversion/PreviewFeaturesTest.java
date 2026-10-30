package com.howtodoinjava.classversion;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class PreviewFeaturesTest {

  @TempDir
  Path out;

  private final Path jdk25 = Path.of(System.getProperty("java.home"));

  @Test
  void givenPreviewClass_whenRunWithoutEnablePreview_thenUnsupportedClassVersionError() throws Exception {
    Jdks.javac(out, "preview", "--release", "25", "--enable-preview");

    Jdks.Result run = Jdks.java(jdk25, "-cp", out.toString(), "com.howtodoinjava.Preview");

    assertThat(run.output()).contains("java.lang.UnsupportedClassVersionError: Preview features are not enabled "
        + "for com/howtodoinjava/Preview (class file version 69.65535). Try running with '--enable-preview'");
  }

  @Test
  void givenPreviewClass_whenRunWithEnablePreview_thenItRuns() throws Exception {
    Jdks.javac(out, "preview", "--release", "25", "--enable-preview");

    Jdks.Result run = Jdks.java(jdk25, "--enable-preview", "-cp", out.toString(), "com.howtodoinjava.Preview");

    assertThat(run.exitCode()).isZero();
    assertThat(run.output()).isEqualToIgnoringNewLines("Primitive pattern matched 42");
  }
}
