package com.howtodoinjava.upgrade.demo;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * Off-heap memory access through sun.misc.Unsafe, the way older libraries do it.
 * JDK 25 prints a warning the first time a memory-access method is called (JEP 498).
 */
public class UnsafeDemo {

  public static void main(String[] args) throws Exception {
    Field field = Unsafe.class.getDeclaredField("theUnsafe");
    field.setAccessible(true);
    Unsafe unsafe = (Unsafe) field.get(null);

    long address = unsafe.allocateMemory(8);
    unsafe.putLong(address, 42L);
    System.out.println("value = " + unsafe.getLong(address));
    unsafe.freeMemory(address);
  }
}
