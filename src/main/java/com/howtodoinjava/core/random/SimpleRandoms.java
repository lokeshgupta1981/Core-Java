package com.howtodoinjava.core.random;

import java.util.Random;
import java.util.SplittableRandom;
import java.util.UUID;

public class SimpleRandoms {
    public static void main(String[] args) {
        Random randomGenerator = new Random();

        // Upper bound 10000
        System.out.println(randomGenerator.nextInt(10_000));
        System.out.println(randomGenerator.nextInt(10_000));

        // No upper bound
        System.out.println(randomGenerator.nextInt());
        System.out.println(randomGenerator.nextInt());

        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);

        SplittableRandom random = new SplittableRandom();
        SplittableRandom splitted = random.split();

        System.out.println(random.nextInt(10000));
        System.out.println(splitted.nextInt(10000));
        System.out.println(random.nextInt(10000));
        System.out.println(splitted.nextInt(10000));
    }
}
