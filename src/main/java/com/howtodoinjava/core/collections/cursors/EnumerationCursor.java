package com.howtodoinjava.core.collections.cursors;

import java.util.Enumeration;
import java.util.Vector;

public class EnumerationCursor {

    public static void main(String[] args) {

        Vector<Integer> vector = new Vector<>();

        for(int i=1;i<=10;i++){ vector.addElement(i); }

        Enumeration<Integer> enumeration = vector.elements();

        while(enumeration.hasMoreElements()){

            System.out.print(enumeration.nextElement() + " ");

        }

    }

}
