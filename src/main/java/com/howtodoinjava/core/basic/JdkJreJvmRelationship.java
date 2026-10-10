package com.howtodoinjava.core.basic;

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
import java.math.*;
import javax.tools.ToolProvider;

/**
 * Examples for the tutorial "Difference Between JDK, JRE and JVM in Java (Java 25)".
 * https://howtodoinjava.com/java/basics/jdk-jre-jvm/
 */
public class JdkJreJvmRelationship {

    public static void main(String[] args) throws Exception {
        {
            String jvm = System.getProperty("java.vm.name");            // "OpenJDK 64-Bit Server VM"
            show("jvm", jvm);
            String runtime = System.getProperty("java.runtime.name");   // "OpenJDK Runtime Environment"
            show("runtime", runtime);
            boolean jdk = ToolProvider.getSystemJavaCompiler() != null; // true on a JDK, false on a runtime without jdk.compiler
            show("jdk", jdk);
        }
        {
            ClassLoader forString = String.class.getClassLoader();                // null (bootstrap class loader)
            show("forString", forString);
            String forSql = java.sql.Connection.class.getClassLoader().getName(); // "platform"
            show("forSql", forSql);
            String forApp = ClassLoader.getSystemClassLoader().getName();         // "app"
            show("forApp", forApp);
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
