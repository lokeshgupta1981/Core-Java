package com.howtodoinjava.java25.concurrency;

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

/**
 * Examples for the tutorial "Java Scoped Values Explained with Examples (Java 25)".
 * https://howtodoinjava.com/java/multi-threading/java-scoped-values/
 */
public class ScopedValuesGuide {
    static final ScopedValue<String> TENANT = ScopedValue.newInstance();

    static String findInvoices() {
        return "invoices of " + TENANT.get();
    }
    static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

    static String insideParentThread() {
        return "request " + REQUEST_ID.get();
    }
    static final InheritableThreadLocal<String> INHERITED_ID = new InheritableThreadLocal<>();
    static final ScopedValue<String> CONTEXT = ScopedValue.newInstance();

    static String doSomething() {
        return "doSomething() sees " + CONTEXT.get();
    }
    static String repositoryQuery() {
        return "SELECT * FROM invoice WHERE tenant = '" + TENANT.get() + "'";
    }

    static String serviceCall() {
        return repositoryQuery();
    }

    static String handleRequest(String tenantFromToken) {
        return ScopedValue.where(TENANT, tenantFromToken).call(() -> serviceCall());
    }
    static String doSomethingAgain() {
        return CONTEXT.get();
    }

    static String rebind() {
        String inner = ScopedValue.where(CONTEXT, "Changed Value").call(() -> doSomethingAgain());
        return CONTEXT.get() + " / " + inner + " / " + CONTEXT.get();
    }
    static record RequestContext(String user, String role, String region) {}
    static final ScopedValue<RequestContext> REQUEST = ScopedValue.newInstance();
    static final ScopedValue<String> TRACE_ID = ScopedValue.newInstance();

    static String audit() {
        return REQUEST.get().user() + " in " + REQUEST.get().region() + " [" + TRACE_ID.get() + "]";
    }
    static String childView(String label) {
        return label + ":" + CONTEXT.orElse("unbound");
    }
    static String forkChildren() throws InterruptedException {
        try (var scope = StructuredTaskScope.open()) {
            var first = scope.fork(() -> childView("child1"));
            var second = scope.fork(() -> childView("child2"));
            scope.join();
            return first.get() + ", " + second.get();
        }
    }
    static String submitToExecutor() throws Exception {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            return executor.submit(() -> childView("pooled")).get();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String invoices = ScopedValue.where(TENANT, "acme").call(() -> findInvoices());   // "invoices of acme"
            show("invoices", invoices);
            boolean boundOutside = TENANT.isBound();                                          // false
            show("boundOutside", boundOutside);
        }
        {
            AtomicReference<String> seen = new AtomicReference<>();
            Thread parentThread = new Thread(() -> {
                REQUEST_ID.set("TestValue");
                try {
                    seen.set(insideParentThread());
                } finally {
                    REQUEST_ID.remove();                // avoids leaks on pooled threads
                }
            });
            parentThread.start();
            parentThread.join();
            String inParent = seen.get();               // "request TestValue"
            show("inParent", inParent);
        }
        {
            AtomicReference<String> seenByChild = new AtomicReference<>();
            Thread parent = new Thread(() -> {
                REQUEST_ID.set("TestValue");
                Thread child = new Thread(() -> seenByChild.set(String.valueOf(REQUEST_ID.get())));
                child.start();
                try {
                    child.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    REQUEST_ID.remove();
                }
            });
            parent.start();
            parent.join();
            String inChild = seenByChild.get();         // "null"
            show("inChild", inChild);
        }
        {
            AtomicReference<String> inherited = new AtomicReference<>();
            INHERITED_ID.set("TestValue");
            Thread worker = new Thread(() -> inherited.set(INHERITED_ID.get()));
            worker.start();
            worker.join();
            INHERITED_ID.remove();
            String inWorker = inherited.get();          // "TestValue"
            show("inWorker", inWorker);
        }
        {
            AtomicReference<String> fromRun = new AtomicReference<>();
            ScopedValue.where(CONTEXT, "TestValue").run(() -> fromRun.set(doSomething()));
            String ran = fromRun.get();                                               // "doSomething() sees TestValue"
            show("ran", ran);
            String called = ScopedValue.where(CONTEXT, "Other").call(() -> doSomething()); // "doSomething() sees Other"
            show("called", called);
        }
        {
            String sql = handleRequest("acme");      // "SELECT * FROM invoice WHERE tenant = 'acme'"
            show("sql", sql);
        }
        {
            boolean bound = CONTEXT.isBound();                                          // false
            show("bound", bound);
            String fallback = CONTEXT.orElse("none");                                   // "none"
            show("fallback", fallback);
            try { String missing = CONTEXT.get(); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
            try { String required = CONTEXT.orElseThrow(() -> new IllegalStateException("no context")); show("required", required); } catch (Throwable _t) { System.out.println("required -> " + _t); }
            try { String nullFallback = CONTEXT.orElse(null); show("nullFallback", nullFallback); } catch (Throwable _t) { System.out.println("nullFallback -> " + _t); }
        }
        {
            String values = ScopedValue.where(CONTEXT, "Test Value").call(() -> rebind()); // "Test Value / Changed Value / Test Value"
            show("values", values);
        }
        {
            RequestContext ctx = new RequestContext("lokesh", "admin", "eu");
            String line = ScopedValue.where(REQUEST, ctx).where(TRACE_ID, "t-42").call(() -> audit()); // "lokesh in eu [t-42]"
            show("line", line);
        }
        {
            String children = ScopedValue.where(CONTEXT, "TestValue").call(() -> forkChildren());   // "child1:TestValue, child2:TestValue"
            show("children", children);
        }
        {
            String viaExecutor = ScopedValue.where(CONTEXT, "TestValue").call(() -> submitToExecutor()); // "pooled:unbound"
            show("viaExecutor", viaExecutor);
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
