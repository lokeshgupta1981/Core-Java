package com.howtodoinjava.core.collections.list;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Equator;
import org.hamcrest.Matchers;

public class EqualListsIgnoringOrder {

  public static void main(String[] args) {

    List<String> list = Arrays.asList("a", "b", "c");
    List<String> equalList = Arrays.asList("b", "c", "a");
    List<String> unequalList = Arrays.asList("d", "c", "a");

    //1 - Common-collections4
    assertTrue(CollectionUtils.isEqualCollection(list, equalList));
    assertFalse(CollectionUtils.isEqualCollection(list, unequalList));

    assertFalse(CollectionUtils.isEqualCollection(
        Arrays.asList("a", "a", "b"),
        Arrays.asList("a", "b", "b")));

    assertTrue(CollectionUtils.isEqualCollection(
        Arrays.asList("a", "b", "b"),
        Arrays.asList("a", "b", "b")));

    assertTrue(CollectionUtils.isEqualCollection(
        Arrays.asList("a", "b"),
        Arrays.asList("A", "B"),
        new StringCaseEquator()));

    //2 - Hamcrest
    assertThat(list, Matchers.containsInAnyOrder(equalList.toArray()));

    //3 - AssertJ

    List<String> firstList = Arrays.asList("a", "b", "c", "d");
    List<String> secondList = Arrays.asList("b", "c", "a", "d");

    org.assertj.core.api.Assertions.assertThat(firstList)
        .containsExactlyInAnyOrderElementsOf(secondList);

    //4 - Junit

    firstList = Arrays.asList("a", "b", "c", "c");
    secondList = Arrays.asList("b", "c", "a", "c");

    assertTrue(compareListsIgnoringOrder(new ArrayList(firstList), new ArrayList<>(secondList)));

  }

  static boolean compareListsIgnoringOrder(ArrayList list1, ArrayList list2) {

    if (list1 == null || list2 == null) return false;
    if (list1.size() != list2.size()) return false;

    for (Object o : list1) {
      list2.remove(o);
    }

    if (list2.size() != 0) return false;
    return true;
  }

}

class StringCaseEquator implements Equator<String> {

  public boolean equate(String s1, String s2) {
    return s1.equalsIgnoreCase(s2);
  }

  @Override
  public int hash(String s) {
    return s.toUpperCase().hashCode();
  }
}

