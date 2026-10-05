package com.howtodoinjava.java25;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

/** Runs the files in scripts/ with the source launcher, as a reader would. */
class CompactSourceFilesTest {

  @Test
  void instanceMainWithScopedValue() throws Exception {
    Jvm.Result r = Jvm.java(List.of("scripts/Library.java"));
    assertThat(r.exitCode()).isZero();
    assertThat(r.output()).isEqualToIgnoringNewLines("Hello, LokeshBound after run(): false");
  }

  @Test
  void ioReadlnAndAutomaticJavaBaseImport() throws Exception {
    Jvm.Result r = Jvm.run(Jvm.tool("java"), List.of("scripts/Greeting.java"), "Lokesh\n");
    assertThat(r.exitCode()).isZero();
    assertThat(r.output()).isEqualToIgnoringNewLines("Your name: Hello Lokesh, we have 3 books");
  }

  @Test
  void ambiguousSimpleNameFailsToCompile() throws Exception {
    Jvm.Result r = Jvm.java(List.of("scripts/AmbiguousImports.java"));
    assertThat(r.exitCode()).isEqualTo(1);
    assertThat(r.output())
        .contains("error: reference to List is ambiguous")
        .contains("both class java.awt.List in java.awt and interface java.util.List in java.util match");
  }

  @Test
  void singleTypeImportResolvesAmbiguity() throws Exception {
    Jvm.Result r = Jvm.java(List.of("scripts/FixedImports.java"));
    assertThat(r.exitCode()).isZero();
    assertThat(r.output().strip()).isEqualTo("[Dune, Emma]");
  }

  @Test
  void previewSnippetsRunWithEnablePreview() throws Exception {
    Jvm.Result r = Jvm.java(List.of("--enable-preview", "--source", "25", "scripts/PreviewSnippets.java"));
    assertThat(r.exitCode()).isZero();
    assertThat(r.output()).contains("medium | ready to borrow | too large for int: 3000000000 | false")
        .contains("Library opened");
  }

  @Test
  void previewClassNeedsEnablePreview() throws Exception {
    Jvm.Result r = Jvm.java(List.of("-cp", Jvm.classes(), "com.howtodoinjava.java25.AllFeatures"));
    assertThat(r.exitCode()).isEqualTo(1);
    assertThat(r.output()).contains("Preview features are not enabled")
        .contains("(class file version 69.65535)");
  }
}
