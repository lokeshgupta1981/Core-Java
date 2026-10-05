package com.howtodoinjava.app;

import com.howtodoinjava.greeter.Greeter;
import com.howtodoinjava.report.ReportHeader;
import com.howtodoinjava.welcome.WelcomeService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.security.CodeSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The app module resolves greeter 1.0.0 (legacy-report is declared first),
 * but welcome-service was compiled against greeter 2.0.0.
 */
class VersionConflictTest {

  @Test
  void greeterComesFromVersion100() {
    CodeSource source = Greeter.class.getProtectionDomain().getCodeSource();
    String location = (source != null) ? source.getLocation().toString() : "JDK class";
    assertTrue(location.contains("greeter-1.0.0") || location.contains("greeter-v1"), location);
  }

  @Test
  void legacyReportStillWorks() {
    assertEquals("Hello, Lokesh - weekly report", ReportHeader.header("Lokesh"));
  }

  @Test
  void welcomeServiceThrowsNoSuchMethodError() {
    NoSuchMethodError error = assertThrows(NoSuchMethodError.class,
        () -> WelcomeService.welcome("Lokesh", "es"));
    assertEquals("'java.lang.String com.howtodoinjava.greeter.Greeter.greet(java.lang.String, java.lang.String)'",
        error.getMessage());
  }

  @Test
  void noSuchMethodErrorIsALinkageError() {
    NoSuchMethodError error = assertThrows(NoSuchMethodError.class,
        () -> WelcomeService.welcome("Lokesh", "fr"));
    assertTrue(error instanceof IncompatibleClassChangeError);
    assertTrue(error instanceof LinkageError);
  }

  @Test
  void reflectionThrowsNoSuchMethodException() {
    assertThrows(NoSuchMethodException.class,
        () -> Greeter.class.getMethod("greet", String.class, String.class));
  }

  @Test
  void reflectionFallsBackWhenMethodIsMissing() {
    String text;
    try {
      Method greet = Greeter.class.getMethod("greet", String.class, String.class);
      text = (String) greet.invoke(null, "Lokesh", "es");
    } catch (NoSuchMethodException e) {
      text = Greeter.greet("Lokesh");
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException("Cannot call greet()", e);
    }
    assertEquals("Hello, Lokesh", text);
  }
}
