package com.howtodoinjava.core.collections.cursors;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;

public class ConcurrentModification {

    public static void main(String[] args) {

        try {

            List<Integer> numbers = new ArrayList<>();

            for (int i = 1; i <= 5; i++) { numbers.add(i); }

            ListIterator<Integer> iterator = numbers.listIterator();

            System.out.println(iterator.next());

            numbers.add(0, 10);

            System.out.println(iterator.next());

        } catch (ConcurrentModificationException exception) {

            System.out.println("Attempted to the modify collection");

            System.out.println(exception);

        }

    }

}
