package com.howtodoinjava.misc;

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
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Java Reflection Examples: Real-World Uses With Code".
 * https://howtodoinjava.com/java/real-usage-examples-of-reflection-in-java/
 */
public class ReflectionApiOverview {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface TestCase {}
    static class CartTests {
        @TestCase
        void emptyCartIsFree() {
            int total = 0;
            if (total != 0) throw new AssertionError("expected 0 but was " + total);
        }

        @TestCase
        void oddItemIsNotLost() {
            int total = 7 / 2 * 2;
            if (total != 7) throw new AssertionError("expected 7 but was " + total);
        }

        void helper() {}
    }
    static List<String> runTests(Class<?> testClass) throws ReflectiveOperationException {
        Object instance = testClass.getDeclaredConstructor().newInstance();
        Method[] methods = testClass.getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));
        List<String> results = new ArrayList<>();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(TestCase.class)) {
                continue;
            }
            try {
                method.invoke(instance);
                results.add("PASS " + method.getName());
            } catch (InvocationTargetException e) {
                results.add("FAIL " + method.getName() + ": " + e.getCause().getMessage());
            }
        }
        return results;
    }
    static record Book(String title, int pages) {}
    static record Member(String name, int age) {}
    static List<String> toCsv(List<? extends Record> rows) throws ReflectiveOperationException {
        RecordComponent[] columns = rows.getFirst().getClass().getRecordComponents();
        List<String> lines = new ArrayList<>();
        lines.add(Arrays.stream(columns).map(RecordComponent::getName).collect(Collectors.joining(",")));
        for (Record row : rows) {
            List<String> cells = new ArrayList<>();
            for (RecordComponent column : columns) {
                cells.add(String.valueOf(column.getAccessor().invoke(row)));
            }
            lines.add(String.join(",", cells));
        }
        return lines;
    }
    static class EmailSender {
        String send(String to) {
            return "welcome mail to " + to;
        }
    }

    static class SignupService {
        private final EmailSender sender;

        SignupService(EmailSender sender) {
            this.sender = sender;
        }

        String register(String name) {
            return sender.send(name);
        }
    }
    static <T> T create(Class<T> type) throws ReflectiveOperationException {
        Constructor<?> constructor = type.getDeclaredConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            arguments[i] = create(parameterTypes[i]);
        }
        return type.cast(constructor.newInstance(arguments));
    }
    static <T> T loadPlugin(String className, Class<T> type) throws ReflectiveOperationException {
        Class<? extends T> implementation = Class.forName(className).asSubclass(type);
        return implementation.getDeclaredConstructor().newInstance();
    }
    static interface PaymentGateway {
        String charge(String user, int amount);
    }

    static class FakeGateway implements PaymentGateway {
        public String charge(String user, int amount) {
            return "charged " + user + " " + amount;
        }
    }
    static <T> T withLogging(T target, Class<T> type, List<String> log) {
        InvocationHandler handler = (proxy, method, args) -> {
            log.add("call " + method.getName() + Arrays.toString(args));
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        };
        Object proxy = Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, handler);
        return type.cast(proxy);
    }
    static class Wallet {
        private int balance = 100;
    }
    static class TextLength {
        private static final MethodHandle LENGTH = findLength();

        private static MethodHandle findLength() {
            try {
                return MethodHandles.lookup()
                        .findVirtual(String.class, "length", MethodType.methodType(int.class));
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(e);
            }
        }

        static int of(String text) {
            try {
                return (int) LENGTH.invokeExact(text);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Class<?> type = Class.forName("java.util.ArrayList");
            Object list = type.getDeclaredConstructor().newInstance();
            Object added = type.getMethod("add", Object.class).invoke(list, "apple");   // true
            show("added", added);
            Object size = type.getMethod("size").invoke(list);                          // 1
            show("size", size);
            String simpleName = type.getSimpleName();                                   // "ArrayList"
            show("simpleName", simpleName);
        }
        {
            List<String> report = runTests(CartTests.class);   // [PASS emptyCartIsFree, FAIL oddItemIsNotLost: expected 7 but was 6]
            show("report", report);
        }
        {
            List<String> books = toCsv(List.of(new Book("Dune", 412), new Book("Emma", 474)));   // [title,pages, Dune,412, Emma,474]
            show("books", books);
            List<String> members = toCsv(List.of(new Member("Lokesh", 37)));                      // [name,age, Lokesh,37]
            show("members", members);
        }
        {
            SignupService service = create(SignupService.class);
            String mail = service.register("Lokesh");             // "welcome mail to Lokesh"
            show("mail", mail);
        }
        {
            CharSequence text = loadPlugin("java.lang.StringBuilder", CharSequence.class);
            int length = text.length();                                                   // 0
            show("length", length);
            try { CharSequence wrongType = loadPlugin("java.util.ArrayList", CharSequence.class); show("wrongType", wrongType); } catch (Throwable _t) { System.out.println("wrongType -> " + _t); }
            try { CharSequence missing = loadPlugin("com.example.PdfExporter", CharSequence.class); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
        }
        {
            List<String> log = new ArrayList<>();
            PaymentGateway gateway = withLogging(new FakeGateway(), PaymentGateway.class, log);
            String receipt = gateway.charge("Lokesh", 40);        // "charged Lokesh 40"
            show("receipt", receipt);
            List<String> calls = log;                             // [call charge[Lokesh, 40]]
            show("calls", calls);
        }
        {
            Field balanceField = Wallet.class.getDeclaredField("balance");
            balanceField.setAccessible(true);
            Wallet wallet = new Wallet();
            int before = balanceField.getInt(wallet);              // 100
            show("before", before);
            balanceField.setInt(wallet, 250);
            int after = balanceField.getInt(wallet);               // 250
            show("after", after);
        }
        {
            Field stringBytes = String.class.getDeclaredField("value");
            boolean opened = stringBytes.trySetAccessible();       // false
            show("opened", opened);
            boolean ownField = Wallet.class.getDeclaredField("balance").trySetAccessible(); // true
            show("ownField", ownField);
        }
        {
            int appleLength = TextLength.of("apple");      // 5
            show("appleLength", appleLength);
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
