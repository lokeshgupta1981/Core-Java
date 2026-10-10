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
 * Examples for the tutorial "Java ThreadLocal: When and How to Use It (With Examples)".
 * https://howtodoinjava.com/java/multi-threading/when-and-how-to-use-thread-local-variables/
 */
public class ThreadLocalExamplesJava25 {
    static class RequestContext {
        private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

        static void set(String id) {
            REQUEST_ID.set(id);
        }

        static String get() {
            return REQUEST_ID.get();
        }

        static void clear() {
            REQUEST_ID.remove();
        }
    }
    static String placeOrder(String item) {
        return "[" + RequestContext.get() + "] order placed for " + item;
    }
    static String handle(String requestId, String item) {
        RequestContext.set(requestId);
        try {
            return placeOrder(item);
        } finally {
            RequestContext.clear();                 // the pool thread serves other requests next
        }
    }
    static class Hashing {
        private static final ThreadLocal<MessageDigest> SHA_256 = ThreadLocal.withInitial(Hashing::newDigest);

        private static MessageDigest newDigest() {
            try {
                return MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e);
            }
        }

        static String sha256(String text) {
            byte[] hash = SHA_256.get().digest(text.getBytes(StandardCharsets.UTF_8));   // digest() also resets it
            return HexFormat.of().formatHex(hash);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            ThreadLocal<String> requestId = ThreadLocal.withInitial(() -> "none");
            requestId.set("req-1");
            CompletableFuture<String> fromPool = CompletableFuture.supplyAsync(requestId::get);
            String inMain = requestId.get();                // "req-1"
            show("inMain", inMain);
            String inPoolThread = fromPool.join();          // "none"
            show("inPoolThread", inPoolThread);
            requestId.remove();
            String afterRemove = requestId.get();           // "none"
            show("afterRemove", afterRemove);
        }
        {
            ExecutorService web = Executors.newFixedThreadPool(2);
            Future<String> request1 = web.submit(() -> handle("req-7", "book"));
            Future<String> request2 = web.submit(() -> handle("req-8", "pen"));
            String log1 = request1.get();                   // "[req-7] order placed for book"
            show("log1", log1);
            String log2 = request2.get();                   // "[req-8] order placed for pen"
            show("log2", log2);
            String leftover = web.submit(() -> RequestContext.get()).get();   // null
            show("leftover", leftover);
            web.close();
        }
        {
            String prefix = Hashing.sha256("abc").substring(0, 8);   // "ba7816bf"
            show("prefix", prefix);
        }
        {
            ThreadLocal<String> loggedInUser = new ThreadLocal<>();
            ExecutorService singleThread = Executors.newSingleThreadExecutor();
            Future<?> firstRequest = singleThread.submit(() -> loggedInUser.set("alice"));
            firstRequest.get();
            Future<String> secondRequest = singleThread.submit(() -> loggedInUser.get());
            String wrongUser = secondRequest.get();         // "alice"
            show("wrongUser", wrongUser);
            singleThread.close();
        }
        {
            InheritableThreadLocal<String> tenant = new InheritableThreadLocal<>();
            ThreadLocal<String> plainTenant = new ThreadLocal<>();
            tenant.set("acme");
            plainTenant.set("acme");
            FutureTask<String> inherited = new FutureTask<>(tenant::get);
            FutureTask<String> notInherited = new FutureTask<>(plainTenant::get);
            Thread.ofPlatform().start(inherited);
            Thread.ofPlatform().start(notInherited);
            String childTenant = inherited.get();           // "acme"
            show("childTenant", childTenant);
            String childPlain = notInherited.get();         // null
            show("childPlain", childPlain);
        }
        {
            ScopedValue<String> currentUser = ScopedValue.newInstance();
            String greeting = ScopedValue.where(currentUser, "alice").call(() -> "hello " + currentUser.get());   // "hello alice"
            show("greeting", greeting);
            boolean boundAfter = currentUser.isBound();     // false
            show("boundAfter", boundAfter);
            String fallback = currentUser.orElse("guest");  // "guest"
            show("fallback", fallback);
            try { String unbound = currentUser.get(); show("unbound", unbound); } catch (Throwable _t) { System.out.println("unbound -> " + _t); }
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
