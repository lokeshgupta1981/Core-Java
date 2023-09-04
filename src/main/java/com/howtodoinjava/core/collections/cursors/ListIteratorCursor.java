package com.howtodoinjava.core.collections.cursors;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListIteratorCursor {

    public static void main(String[] args) {

        List<Integer> immutableList = List.of(1, 2, 3, 4, 5);
        List<Integer> numbers = new ArrayList<>(immutableList);

        ListIterator<Integer> iterator = numbers.listIterator();

        int num;

        while (iterator.hasNext()) {

            num = iterator.next();

            if (num == 1) { iterator.remove(); }

            else if (num == 5){ iterator.set(50); }

        }

        iterator.add(6);

        System.out.println(numbers);

        while (iterator.hasPrevious()) {

            num = iterator.previous();

            if (num == 6) { iterator.remove(); }

            else if (num == 50){ iterator.set(5);}

        }

        iterator.add(1);

        System.out.println(numbers);

    }

}
