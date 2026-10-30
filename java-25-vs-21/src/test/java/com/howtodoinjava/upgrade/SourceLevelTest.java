package com.howtodoinjava.upgrade;

import java.io.StringWriter;
import java.net.URI;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SourceLevelTest {

  @Test
  void givenUnnamedVariable_whenCompilingForJava21_thenError() {
    String source = """
        class Counter {
          int count(java.util.List<String> items) {
            int n = 0;
            for (String _ : items) n++;
            return n;
          }
        }
        """;

    assertThat(compile("Counter", source, "21"))
        .contains("unnamed variables are not supported in -source 21");
    assertThat(compile("Counter", source, "25")).isEmpty();
  }

  @Test
  void givenStreamGatherer_whenCompilingForJava21_thenSymbolIsMissing() {
    String source = """
        import java.util.List;
        import java.util.stream.Gatherers;
        class Batches {
          List<List<Integer>> pairs(List<Integer> xs) {
            return xs.stream().gather(Gatherers.windowFixed(2)).toList();
          }
        }
        """;

    assertThat(compile("Batches", source, "21")).contains("cannot find symbol");
    assertThat(compile("Batches", source, "25")).isEmpty();
  }

  @Test
  void givenModuleImport_whenCompilingForJava21_thenError() {
    String source = """
        import module java.base;
        class Names {
          List<String> names = new ArrayList<>();
        }
        """;

    assertThat(compile("Names", source, "21"))
        .contains("module imports are not supported in -source 21");
    assertThat(compile("Names", source, "25")).isEmpty();
  }

  @Test
  void givenInstanceMainMethod_whenCompilingForJava21_thenError() {
    String source = """
        void main() {
            IO.println("Hello from Java 25");
        }
        """;

    assertThat(compile("Hello", source, "21"))
        .contains("implicitly declared classes are not supported in -source 21");
    assertThat(compile("Hello", source, "25")).isEmpty();
  }

  private static String compile(String className, String source, String release) {
    JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    SimpleJavaFileObject file = new SimpleJavaFileObject(
        URI.create("string:///" + className + ".java"), SimpleJavaFileObject.Kind.SOURCE) {
      @Override
      public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return source;
      }
    };
    StringWriter errors = new StringWriter();
    javac.getTask(errors, null, null, List.of("--release", release, "-proc:none", "-d",
        System.getProperty("java.io.tmpdir")), null, List.of(file)).call();
    return errors.toString();
  }
}
