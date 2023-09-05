package com.howtodoinjava.core.collections.cursors;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class AlternateNextAndPrevious {

    public static void main(String[] args) {

        List<Integer> numbers = new ArrayList<>();

        for (int i = 1; i <= 5; i++) { numbers.add(i); }

        ListIterator<Integer> iterator = numbers.listIterator();

        System.out.println(iterator.next()); //1

        System.out.println(iterator.next()); //2

        System.out.println(iterator.previous()); //2

        System.out.println(iterator.next()); //2

        System.out.println(iterator.previous()); //2

    }
}
