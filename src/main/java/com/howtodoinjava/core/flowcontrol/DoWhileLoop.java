package com.howtodoinjava.core.flowcontrol;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Java do-while Loop with Examples (vs while Loop)".
 * https://howtodoinjava.com/java/flow-control/do-while-loop-in-java/
 */
public class DoWhileLoop {
    static int countPaidWrong(boolean[] paid) {
        int i = 0;
        int count = 0;
        do {
            if (!paid[i]) {
                continue;                   // skips i++, loops forever on false
            }
            count++;
            i++;
        } while (i < paid.length);
        return count;
    }
    static int countPaid(boolean[] paid) {
        if (paid.length == 0) {
            return 0;
        }
        int i = 0;
        int count = 0;
        do {
            boolean current = paid[i];
            i++;
            if (!current) {
                continue;
            }
            count++;
        } while (i < paid.length);
        return count;
    }
    static int addPoints(AtomicInteger balance, int points) {
        int current;
        int updated;
        do {
            current = balance.get();
            updated = Math.min(current + points, 1000);
        } while (!balance.compareAndSet(current, updated));
        return updated;
    }
    public static void main(String[] args) throws Exception {
        {
            Random dice = new Random(42);
            int rolls = 0;
            int face;
            do {
                face = dice.nextInt(1, 7);          // 1 to 6
                rolls++;
            } while (face != 6);
            int rollsNeeded = rolls;                // 7
            show("rollsNeeded", rollsNeeded);
            int lastFace = face;                    // 6
            show("lastFace", lastFace);
        }
        {
            int copies = 0;
            do {
                copies++;
            } while (copies < 3);
            int printedCopies = copies;             // 3
            show("printedCopies", printedCopies);
        }
        {
            int whileRuns = 0;
            int limit = 0;
            while (whileRuns < limit) {
                whileRuns++;
            }
            int doRuns = 0;
            do {
                doRuns++;
            } while (doRuns < limit);
            int whileCount = whileRuns;             // 0
            show("whileCount", whileCount);
            int doCount = doRuns;                   // 1
            show("doCount", doCount);
        }
        {
            Scanner in = new Scanner("abc -4 7");
            int quantity = -1;
            int prompts = 0;
            do {
                prompts++;                          // print "Quantity?" here
                if (in.hasNextInt()) {
                    quantity = in.nextInt();
                } else {
                    in.next();                      // skip a non-number token
                }
            } while (quantity <= 0 && in.hasNext());
            int accepted = quantity;                // 7
            show("accepted", accepted);
            int asked = prompts;                    // 3
            show("asked", asked);
        }
        {
            Set<String> used = new HashSet<>(Set.of("A1", "B2", "C0"));
            Random random = new Random(21);
            String code;
            int tries = 0;
            do {
                code = "" + (char) ('A' + random.nextInt(3)) + random.nextInt(3);
                tries++;
            } while (used.contains(code));
            used.add(code);
            String newCode = code;                  // "B1"
            show("newCode", newCode);
            int attempts = tries;                   // 2
            show("attempts", attempts);
        }
        {
            int paidOrders = countPaid(new boolean[] {true, false, true});   // 2
            show("paidOrders", paidOrders);
            int none = countPaid(new boolean[0]);                            // 0
            show("none", none);
        }
        {
            AtomicInteger balance = new AtomicInteger(990);
            int afterBonus = addPoints(balance, 25);  // 1000, capped
            show("afterBonus", afterBonus);
            int stored = balance.get();               // 1000
            show("stored", stored);
        }
        {
            int tries = 0;
            while (true) {
                tries++;
                if (tries >= 3) {
                    break;
                }
            }
            int loops = tries;                      // 3
            show("loops", loops);
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
