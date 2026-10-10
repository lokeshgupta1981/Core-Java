package com.howtodoinjava.newFeatures;

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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Examples for the tutorial "How to Call an Interface Default Method in Java (With Proxy)".
 * https://howtodoinjava.com/java/calling-interface-default-methods/
 */
public class ProxyInvokeDefaultMethodFlow {
    static interface Swimmable {
        default String travel() {
            return "swims";
        }
    }
    static interface Walkable {
        default String travel() {
            return "walks";
        }
    }
    static class Frog implements Swimmable, Walkable {
        @Override
        public String travel() {
            return Walkable.super.travel() + " and " + Swimmable.super.travel();
        }
    }
    static interface PriceClient {
        int priceOf(String dish);

        default int total(List<String> dishes) {
            return dishes.stream().mapToInt(this::priceOf).sum();
        }
    }
    static PriceClient priceClient(Map<String, Integer> remotePrices) {
        return (PriceClient) Proxy.newProxyInstance(
        PriceClient.class.getClassLoader(),
        new Class<?>[]{PriceClient.class},
        (proxy, method, args) -> {
            if (method.isDefault()) {
                return InvocationHandler.invokeDefault(proxy, method, args);
            }
            return switch (method.getName()) {
                case "priceOf" -> remotePrices.getOrDefault((String) args[0], 0);
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                default -> "PriceClient proxy";
            };
        });
    }
    public static void main(String[] args) throws Exception {
        {
            String frog = new Frog().travel();                                 // "walks and swims"
            show("frog", frog);
        }
        {
            PriceClient client = priceClient(Map.of("soup", 5, "salad", 7));
            int soup = client.priceOf("soup");                                 // 5
            show("soup", soup);
            int order = client.total(List.of("soup", "salad", "soup"));        // 17
            show("order", order);
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
