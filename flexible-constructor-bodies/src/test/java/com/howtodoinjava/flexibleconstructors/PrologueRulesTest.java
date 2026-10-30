package com.howtodoinjava.flexibleconstructors;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrologueRulesTest {

  private static final String BASE = "class Base { Base(int x) { } String name() { return \"b\"; } int size; }\n";

  @Test
  void givenStatementBeforeSuper_whenCompiledForJava21_thenError() {
    String source = BASE + """
        class Child extends Base {
          Child(int x) {
            if (x < 0) throw new IllegalArgumentException();
            super(x);
          }
        }
        """;

    assertThat(compile(source, "21"))
        .containsExactly("flexible constructors is not supported in -source 21\n"
            + "  (use -source 25 or higher to enable flexible constructors)");
  }

  @Test
  void givenStatementBeforeSuper_whenCompiledForJava25_thenNoError() {
    String source = BASE + """
        class Child extends Base {
          Child(int x) {
            if (x < 0) throw new IllegalArgumentException();
            super(x);
          }
        }
        """;

    assertThat(compile(source, "25")).isEmpty();
  }

  @Test
  void whenPrologueReadsField_thenError() {
    assertThat(compileChild("int count; Child() { int copy = count; super(1); }"))
        .containsExactly("cannot reference count before supertype constructor has been called");
  }

  @Test
  void whenPrologueCallsInstanceMethod_thenError() {
    assertThat(compileChild("Child() { String text = name(); super(1); }"))
        .containsExactly("cannot reference name() before supertype constructor has been called");
  }

  @Test
  void whenPrologueUsesThis_thenError() {
    assertThat(compileChild("Child() { System.out.println(this); super(1); }"))
        .containsExactly("cannot reference this before supertype constructor has been called");
  }

  @Test
  void whenPrologueUsesSuperField_thenError() {
    assertThat(compileChild("Child() { int copy = super.size; super(1); }"))
        .containsExactly("cannot reference super before supertype constructor has been called");
  }

  @Test
  void whenPrologueAssignsInitializedField_thenError() {
    assertThat(compileChild("String label = \"none\"; Child() { label = \"sale\"; super(1); }"))
        .containsExactly("cannot assign initialized field 'label' before supertype constructor has been called");
  }

  @Test
  void whenPrologueAssignsUninitializedField_thenNoError() {
    assertThat(compileChild("String label; Child() { label = \"sale\"; super(1); }")).isEmpty();
  }

  @Test
  void whenPrologueReturns_thenError() {
    assertThat(compileChild("Child(boolean skip) { if (skip) return; super(1); }"))
        .containsExactly("'return' not allowed before explicit constructor invocation");
  }

  private static List<String> compileChild(String body) {
    return compile(BASE + "class Child extends Base { " + body + " }\n", "25");
  }

  private static List<String> compile(String source, String release) {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    JavaFileObject file = new SimpleJavaFileObject(URI.create("string:///Child.java"), JavaFileObject.Kind.SOURCE) {
      @Override
      public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return source;
      }
    };
    compiler.getTask(null, null, diagnostics,
        List.of("--release", release, "-d", System.getProperty("java.io.tmpdir") + "/fcb-out"),
        null, List.of(file)).call();
    return diagnostics.getDiagnostics().stream()
        .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
        .map(d -> d.getMessage(Locale.ENGLISH))
        .toList();
  }
}
