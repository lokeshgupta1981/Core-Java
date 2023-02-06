package com.howtodoinjava.core.random;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomNumbersInRange {

  private final static Random RANDOM = new Random();
  private final static SecureRandom SECURE_RANDOM = new SecureRandom();

  public static void main(String[] args) {
    Integer randomInt = getNextRandomInteger(0, 100);
    Float randomFloat = getNextRandomFloat(0.0f, 1.0f);

    Integer secureRandomInt = getNextSecureRandomInteger(0, 100);
    Float secureRandomFloat = getNextSecureRandomFloat(0.0f, 1.0f);

    Integer threadLocalRandomInt = getNextThreadLocalRandomInteger(0, 100);
    Float threadLocalRandomFloat = getNextThreadLocalRandomFloat(0.0f, 1.0f);
  }

  public static Integer getNextRandomInteger(int min, int max) {
    return RANDOM.nextInt(min, max + 1);
  }

  public static Float getNextRandomFloat(float min, float max) {
    return RANDOM.nextFloat(min, max);
  }

  public static Integer getNextSecureRandomInteger(int min, int max) {
    return SECURE_RANDOM.nextInt(min, max + 1);
  }

  public static Float getNextSecureRandomFloat(float min, float max) {
    return SECURE_RANDOM.nextFloat(min, max);
  }

  public static Integer getNextThreadLocalRandomInteger(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }

  public static Float getNextThreadLocalRandomFloat(float min, float max) {
    return ThreadLocalRandom.current().nextFloat(min, max);
  }
}
