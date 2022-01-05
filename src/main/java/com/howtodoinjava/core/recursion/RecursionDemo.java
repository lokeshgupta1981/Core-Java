package com.howtodoinjava.core.recursion;

public class RecursionDemo {
    public static void main(String[] args) {
        var result = sum(1L, 40000L).run();
        System.out.println(result);

        //var result = simpleRecursiveSum(1L, 40000L);
        //System.out.println(result);  //Exception in thread "main" java.lang.StackOverflowError
    }

    static long simpleRecursiveSum(long total, long summand) {
        if (summand == 1L) {
            return total;
        }
        return simpleRecursiveSum(total + summand, summand - 1L);
    }

    static RecursiveCall<Long> sum(Long total, Long summand) {
        if (summand == 1) {
            return RecursiveCall.done(total);
        }
        return () -> sum(total + summand, summand - 1L);
    }
}
