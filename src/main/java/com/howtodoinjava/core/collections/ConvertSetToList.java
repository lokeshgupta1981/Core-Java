package com.howtodoinjava.core.collections;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConvertSetToList {
    public static void main(String[] args) {
        Set<Integer> set = Set.of(1, 2, 3);

        //1.1
        ArrayList<Integer> arrayList = new ArrayList<>(set);
        Assertions.assertEquals(3, arrayList.size());

        //1.2
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        arrayList1.addAll(set);
        Assertions.assertEquals(3, arrayList1.size());

        //1.3
        List<Integer> list3 = set.stream().toList();
        Assertions.assertEquals(3, list3.size());

        //2
        List<Integer> list = List.of(1, 2, 3, 3, 3, 5, 5);

        //2.1
        Set set1 = new HashSet(list);
        Assertions.assertEquals(4, set1.size());

        //2.2
        Set set2 = new HashSet();
        set2.addAll(list);
        Assertions.assertEquals(4, set2.size());

        //2.3
        Set<Integer> set3 = list.stream().collect(Collectors.toSet());

        Assertions.assertEquals(4, set3.size());
    }
}
