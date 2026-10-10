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
import java.util.random.*;
import java.security.*;

/**
 * Examples for the tutorial "Generate Random Numbers in Java With Random and RandomGenerator".
 * https://howtodoinjava.com/java/generate-random-numbers/
 */
public class RandomNumberGeneratorDecisionTree {
    static String checkoutVariant() {
        return ThreadLocalRandom.current().nextInt(100) < 10 ? "new-checkout" : "old-checkout";
    }
    static int rollDice(RandomGenerator generator) {
        return generator.nextInt(1, 7);
    }
    public static void main(String[] args) throws Exception {
        {
            int dice = ThreadLocalRandom.current().nextInt(1, 7);        // 1 to 6
            show("dice", dice);
            double fraction = Math.random();                             // 0.0 to just under 1.0
            show("fraction", fraction);
            boolean coin = RandomGenerator.getDefault().nextBoolean();   // true or false
            show("coin", coin);
            int repeatable = new Random(42).nextInt(100);                // 30, always 30 for seed 42
            show("repeatable", repeatable);
            int code = new SecureRandom().nextInt(100_000, 1_000_000);   // 6-digit code
            show("code", code);
        }
        {
            double fraction = Math.random();                     // 0.0 to just under 1.0
            show("fraction", fraction);
            int percent = (int) (Math.random() * 101);           // 0 to 100
            show("percent", percent);
        }
        {
            Random random = new Random();
            int any = random.nextInt();                 // any int, negative values too
            show("any", any);
            int below = random.nextInt(10);             // 0 to 9
            show("below", below);
            int between = random.nextInt(5, 10);        // 5 to 9
            show("between", between);
            double d = random.nextDouble();             // 0.0 to just under 1.0
            show("d", d);
            boolean flag = random.nextBoolean();        // true or false
            show("flag", flag);
            double gaussian = random.nextGaussian();    // normal distribution around 0.0
            show("gaussian", gaussian);
        }
        {
            Random first = new Random(42);
            Random second = new Random(42);
            int a = first.nextInt(100);                 // 30
            show("a", a);
            int b = second.nextInt(100);                // 30
            show("b", b);
            int next = first.nextInt(100);              // 63
            show("next", next);
        }
        {
            List<String> playlist = new ArrayList<>(List.of("intro", "verse", "chorus", "bridge", "outro"));
            Collections.shuffle(playlist, new Random(7));
            List<String> order = playlist;              // [outro, bridge, intro, chorus, verse]
            show("order", order);
        }
        {
            int[] rolls = new Random(42).ints(5, 1, 7).toArray();         // [3, 4, 1, 3, 1]
            show("rolls", rolls);
            List<Integer> lotto = ThreadLocalRandom.current().ints(1, 50)
                    .distinct()
                    .limit(6)
                    .sorted()
                    .boxed()
                    .toList();                                              // 6 different numbers from 1 to 49
            double average = new Random(42).doubles(10_000).average().orElse(0);  // close to 0.5
            show("average", average);
        }
        {
            String variant = checkoutVariant();                        // "new-checkout" in about 10% of calls
            show("variant", variant);
            try { ThreadLocalRandom.current().setSeed(42);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            SplittableRandom root = new SplittableRandom(42);
            SplittableRandom child = root.split();
            int fromRoot = root.nextInt(1, 7);                          // 1 to 6
            show("fromRoot", fromRoot);
            int fromChild = child.nextInt(1, 7);                        // 1 to 6
            show("fromChild", fromChild);
            long hits = new SplittableRandom(42).ints(1_000_000, 0, 100)
                    .parallel()
                    .filter(n -> n < 25)
                    .count();                                           // close to 250000
        }
        {
            RandomGenerator defaultGenerator = RandomGenerator.getDefault();
            String defaultName = RandomGeneratorFactory.getDefault().name();          // "L32X64MixRandom"
            show("defaultName", defaultName);
            RandomGenerator lxm = RandomGenerator.of("L64X128MixRandom");
            int roll = lxm.nextInt(1, 7);                                              // 1 to 6
            show("roll", roll);
            RandomGenerator seeded = RandomGeneratorFactory.of("Xoshiro256PlusPlus").create(42L);
            long algorithms = RandomGeneratorFactory.all().count();                    // 13
            show("algorithms", algorithms);
        }
        {
            int live = rollDice(ThreadLocalRandom.current());          // 1 to 6
            show("live", live);
            int inTest = rollDice(new Random(42));                     // 3, always 3 for seed 42
            show("inTest", inTest);
        }
        {
            SecureRandom secure = new SecureRandom();
            byte[] bytes = new byte[16];
            secure.nextBytes(bytes);
            String hexToken = HexFormat.of().formatHex(bytes);                          // 32 hex digits
            show("hexToken", hexToken);
            String urlToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);  // 22 URL-safe characters
            show("urlToken", urlToken);
            int otp = secure.nextInt(100_000, 1_000_000);                               // 6-digit code
            show("otp", otp);
            String algorithm = secure.getAlgorithm();                                   // "NativePRNG"
            show("algorithm", algorithm);
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
