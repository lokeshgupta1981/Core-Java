package com.howtodoinjava.misc;

import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

public class TupleExamples {
    @SuppressWarnings("unused")
    public static void main(final String[] args) {

        Pair<String, Integer> pair = Pair.with("Sajal", 12);

        System.out.println("Name : " + pair.getValue0());
        System.out.println("Exp : " + pair.getValue1());

        Triplet<String, Integer, String> triplet = pair.add("SSS");
        System.out.println(triplet);

        Pair<String, Integer> modifiedPair = pair.setAt0("Kajal");

        System.out.println(modifiedPair);

        Pair<String, Integer> person = new Pair<>("Sajal", 12);

        List<String> listOf4Names = Arrays.asList("A1", "A2", "A3", "A4");
        Pair<String, String> pair1 = Pair.fromIterable(listOf4Names, 2);
        System.out.println(pair1);
        Quartet<String, String, String, String> quartet = Quartet
            .fromCollection(listOf4Names);
        System.out.println(quartet);

        Quartet<String, Integer, String, Double> quartet1 = Quartet.with("A1",
            1, "A3", 2.3);

        for (Object obj : quartet1) {
            System.out.println(obj);
        }

        List<Object> quartletList = quartet1.toList();
        System.out.println(quartletList);

        Object[] quartletArr = quartet1.toArray();
        System.out.println(quartletArr);

    }
}
