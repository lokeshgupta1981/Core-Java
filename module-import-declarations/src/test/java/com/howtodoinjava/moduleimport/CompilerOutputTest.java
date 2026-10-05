package com.howtodoinjava.moduleimport;

import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

/** Compiles the files in examples/ with javac and checks the real compiler messages. */
class CompilerOutputTest {

  @TempDir
  Path out;

  private record Result(boolean success, List<String> messages) {}

  private Result compile(String file, String... extraOptions) throws Exception {
    JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    try (StandardJavaFileManager files = javac.getStandardFileManager(diagnostics, Locale.ENGLISH, null)) {
      List<String> options = new ArrayList<>(List.of(extraOptions));
      options.addAll(List.of("-d", out.toString()));
      Iterable<? extends JavaFileObject> units = files.getJavaFileObjects(Path.of("examples", file));
      boolean ok = javac.getTask(new StringWriter(), files, diagnostics, options, null, units).call();
      List<String> messages = diagnostics.getDiagnostics().stream()
          .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
          .map(d -> file + ":" + d.getLineNumber() + ": " + d.getMessage(Locale.ENGLISH))
          .toList();
      messages.forEach(System.out::println);
      return new Result(ok, messages);
    }
  }

  @Test
  void twoModulesExportingListMakeTheNameAmbiguous() throws Exception {
    Result result = compile("AmbiguousList.java");

    assertThat(result.success()).isFalse();
    assertThat(result.messages()).first().asString()
        .contains("reference to List is ambiguous")
        .contains("both class java.awt.List in java.awt and interface java.util.List in java.util match");
  }

  @Test
  void packageImportShadowsModuleImports() throws Exception {
    assertThat(compile("OnDemandFix.java").success()).isTrue();
  }

  @Test
  void javaSqlDateClashesWithJavaUtilDate() throws Exception {
    Result result = compile("AmbiguousDate.java");

    assertThat(result.success()).isFalse();
    assertThat(result.messages()).first().asString()
        .contains("reference to Date is ambiguous")
        .contains("both class java.sql.Date in java.sql and class java.util.Date in java.util match");
  }

  @Test
  void javaSeNeedsAddModulesOnTheClassPath() throws Exception {
    Result withoutFlag = compile("WholeApi.java");
    assertThat(withoutFlag.success()).isFalse();
    assertThat(withoutFlag.messages()).first().asString().contains("unnamed module does not read: java.se");

    assertThat(compile("WholeApi.java", "--add-modules", "java.se").success()).isTrue();
  }

  @Test
  void compactSourceFileImportsJavaBaseImplicitly() throws Exception {
    assertThat(Files.readAllLines(Path.of("examples", "Hello.java"))).noneMatch(line -> line.startsWith("import"));
    assertThat(compile("Hello.java").success()).isTrue();
  }
}
