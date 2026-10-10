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
 * Examples for the tutorial "REST API Security Guide: Checklist With OWASP API Top 10".
 * https://howtodoinjava.com/java/java-security/rest-api-security-guide/
 */
public class RestApiSecurityRequestLayers {
    static record Invoice(long id, String owner, int amountCents, boolean paid) {}
    static Optional<Invoice> findForCaller(Map<Long, Invoice> invoices, long id, String caller) {
        return Optional.ofNullable(invoices.get(id))
                .filter(invoice -> invoice.owner().equals(caller));
    }
    static record InvoiceView(long id, int amountCents, boolean paid) {}        // response: no internal fields
    static final class TokenBucket {
        private final long capacity;
        private final long nanosPerToken;
        private long tokens;
        private long lastRefill;

        TokenBucket(long capacity, Duration refillEvery, long nowNanos) {
            this.capacity = capacity;
            this.nanosPerToken = refillEvery.toNanos();
            this.tokens = capacity;
            this.lastRefill = nowNanos;
        }

        synchronized boolean tryConsume(long nowNanos) {
            long newTokens = (nowNanos - lastRefill) / nanosPerToken;
            if (newTokens > 0) {
                tokens = Math.min(capacity, tokens + newTokens);
                lastRefill += newTokens * nanosPerToken;
            }
            if (tokens == 0) {
                return false;
            }
            tokens--;
            return true;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Map<Long, Invoice> invoices = Map.of(
            1L, new Invoice(1, "lokesh", 4500, false),
            2L, new Invoice(2, "alex", 1200, true));
            Optional<Invoice> own = findForCaller(invoices, 1, "lokesh");      // Optional[Invoice[id=1, owner=lokesh, amountCents=4500, paid=false]]
            show("own", own);
            Optional<Invoice> other = findForCaller(invoices, 2, "lokesh");    // Optional.empty, respond 404
            show("other", other);
        }
        {
            TokenBucket bucket = new TokenBucket(2, Duration.ofSeconds(1), 0);
            boolean first = bucket.tryConsume(0);                                  // true
            show("first", first);
            boolean second = bucket.tryConsume(0);                                 // true
            show("second", second);
            boolean third = bucket.tryConsume(0);                                  // false, respond 429
            show("third", third);
            boolean afterOneSecond = bucket.tryConsume(Duration.ofSeconds(1).toNanos());   // true
            show("afterOneSecond", afterOneSecond);
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
