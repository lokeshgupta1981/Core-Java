package com.howtodoinjava.core.string;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.temporal.*;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Examples for the tutorial "Convert Exception Stack Trace to String in Java".
 * https://howtodoinjava.com/java/string/convert-stacktrace-to-string/
 */
public class StackTraceToString {
    static String stackTraceOf(Throwable error) {
        StringWriter sw = new StringWriter();
        error.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    static String shortTrace(Throwable error, int maxFrames) {
        String frames = Arrays.stream(error.getStackTrace())
                .limit(maxFrames)
                .map(frame -> "    at " + frame)
                .collect(Collectors.joining("\n"));
        return error + "\n" + frames;
    }
    static Throwable rootCause(Throwable error) {
        Throwable cause = error;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
    static String fitColumn(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
    public static void main(String[] args) throws Exception {
        {
            Exception error = new IllegalStateException("Payment failed");
            StringWriter sw = new StringWriter();
            error.printStackTrace(new PrintWriter(sw));
            String trace = sw.toString();
            boolean header = trace.startsWith("java.lang.IllegalStateException: Payment failed");   // true
            show("header", header);
            boolean frames = trace.contains("\tat ");                                              // true
            show("frames", frames);
        }
        {
            Throwable failure = new IllegalStateException("Report rendering failed", new FileNotFoundException("monthly.tpl"));
            String text = stackTraceOf(failure);
            boolean hasHeader = text.startsWith("java.lang.IllegalStateException: Report rendering failed");   // true
            show("hasHeader", hasHeader);
            boolean hasCause = text.contains("Caused by: java.io.FileNotFoundException: monthly.tpl");          // true
            show("hasCause", hasCause);
        }
        {
            Throwable jobError = new IllegalStateException("Report rendering failed", new FileNotFoundException("monthly.tpl"));
            String compact = shortTrace(jobError, 3);
            boolean firstLine = compact.startsWith("java.lang.IllegalStateException: Report rendering failed");   // true
            show("firstLine", firstLine);
            long frameLines = compact.lines().filter(line -> line.startsWith("    at ")).count();
            boolean limited = frameLines <= 3;                                                              // true
            show("limited", limited);
            try { String root = rootCause(jobError).toString(); show("root", root); } catch (Throwable _t) { System.out.println("root -> " + _t); }
        }
        {
            List<String> callers = StackWalker.getInstance()
                    .walk(stack -> stack.limit(3).map(StackWalker.StackFrame::toString).toList());
            boolean hasCallers = !callers.isEmpty();                                   // true
            show("hasCallers", hasCallers);
            String callerText = String.join("\n", callers);
            StackTraceElement[] threadFrames = Thread.currentThread().getStackTrace();
            String firstFrame = threadFrames[0].getMethodName();                       // "getStackTrace"
            show("firstFrame", firstFrame);
        }
        {
            Throwable runError = new IllegalStateException("Report rendering failed", new FileNotFoundException("monthly.tpl"));
            String detail = fitColumn(stackTraceOf(runError), 4000);
            boolean fits = detail.length() <= 4000;                                      // true
            show("fits", fits);
            String tooLong = fitColumn("x".repeat(5000), 4000);
            int storedLength = tooLong.length();                                         // 4000
            show("storedLength", storedLength);
        }
        {
            Throwable libError = new IllegalStateException("Report rendering failed", new FileNotFoundException("monthly.tpl"));
            String libTrace = ExceptionUtils.getStackTrace(libError);
            boolean sameText = libTrace.equals(stackTraceOf(libError));                  // true
            show("sameText", sameText);
            try { String rootMessage = ExceptionUtils.getRootCauseMessage(libError); show("rootMessage", rootMessage); } catch (Throwable _t) { System.out.println("rootMessage -> " + _t); }
        }
        {
            Throwable sample = new IllegalStateException("Payment failed");
            String message = sample.getMessage();       // "Payment failed"
            show("message", message);
            try { String summary = sample.toString(); show("summary", summary); } catch (Throwable _t) { System.out.println("summary -> " + _t); }
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
