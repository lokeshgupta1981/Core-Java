package com.howtodoinjava.core.security;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;

/**
 * Examples for the tutorial "Set Windows Environment Variables Without Admin Rights".
 * https://howtodoinjava.com/java/java-security/windows-set-environment-variables-without-admin-access/
 */
public class WindowsUserVsSystemEnvironmentVariables {

    public static void main(String[] args) throws Exception {
        {
            String javaHome = Optional.ofNullable(System.getenv("JAVA_HOME")).orElse("not set");   // C:\Users\lokesh\tools\jdk-25 on our laptop
            show("javaHome", javaHome);
            String runtime = System.getProperty("java.home");                                         // folder of the JDK running this code
            show("runtime", runtime);
        }
        {
            Map<String, String> current = System.getenv();
            try { String added = current.put("APP_MODE", "dev"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            ProcessBuilder builder = new ProcessBuilder("mvn", "-v");
            Map<String, String> env = builder.environment();
            env.put("JAVA_HOME", "C:\\Users\\lokesh\\tools\\jdk-25");
            String childJavaHome = env.get("JAVA_HOME");            // "C:\Users\lokesh\tools\jdk-25"
            show("childJavaHome", childJavaHome);
            boolean parentUnchanged = !"C:\\Users\\lokesh\\tools\\jdk-25".equals(System.getenv("JAVA_HOME"));   // true
            show("parentUnchanged", parentUnchanged);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
