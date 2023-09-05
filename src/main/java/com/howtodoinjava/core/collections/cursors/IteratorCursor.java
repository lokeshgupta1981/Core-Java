package com.howtodoinjava.core.collections.cursors;

import java.util.*;

public class IteratorCursor {

    public static void main(String[] args) {

        Set<Integer> immutableSet = Set.of(1,2,3,4,5,6,7,8,9,10); // Immutable Set
        Set<Integer> elements = new HashSet<>(immutableSet);  // Mutable Set

        Iterator<Integer> iterator = elements.iterator();

        int num;

        while (iterator.hasNext()){

            if ((num = iterator.next()) == 5){ iterator.remove(); }

            else { System.out.print(num + " "); }
        }

        System.out.println("\n"+elements);

    }

}
