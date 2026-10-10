package com.howtodoinjava.core.collections.list;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;
import org.eclipse.collections.impl.bag.mutable.HashBag;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Examples for the tutorial "Assert Two Lists Equal Ignoring Order (AssertJ, Hamcrest, JUnit)".
 * https://howtodoinjava.com/java/collections/arraylist/assert-two-equal-lists-ignoring-order/
 */
public class AssertListsIgnoringOrder {
    static <T> Map<T, Long> frequencies(List<T> list) {
        return list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
    static record User(long id, String name) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> actual = List.of("viewer", "admin", "editor");
            List<String> expected = List.of("admin", "editor", "viewer");

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);   // passes
            assertEquals(expected.stream().sorted().toList(), actual.stream().sorted().toList());   // passes
            try { assertEquals(expected, actual);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> actual = List.of("viewer", "admin", "editor");
            List<String> expected = List.of("admin", "editor", "viewer");
            assertThat(actual, containsInAnyOrder(expected.toArray()));
            try { assertThat(List.of("a", "a", "b"), containsInAnyOrder("a", "b", "b"));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> roles = List.of("ADMIN", "Viewer");
            assertThat(roles, containsInAnyOrder(equalToIgnoringCase("viewer"), equalToIgnoringCase("admin")));
        }
        {
            List<String> actual = List.of("viewer", "admin", "editor");
            assertThat(actual).containsExactlyInAnyOrderElementsOf(List.of("admin", "editor", "viewer"));
            assertThat(actual).containsExactlyInAnyOrder("editor", "viewer", "admin");
            try { assertThat(List.of("admin", "viewer")).containsExactlyInAnyOrderElementsOf(List.of("admin", "guest"));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            assertThat(List.of("a", "a", "b")).hasSameElementsAs(List.of("a", "b", "b"));   // passes, counts ignored
            assertThat(List.of("ADMIN", "Viewer")).usingElementComparator(String.CASE_INSENSITIVE_ORDER)
                    .containsExactlyInAnyOrderElementsOf(List.of("viewer", "admin"));
        }
        {
            List<String> actual = List.of("viewer", "admin", "editor");
            List<String> expected = List.of("admin", "editor", "viewer");
            assertEquals(expected.stream().sorted().toList(), actual.stream().sorted().toList());
            try { assertEquals(List.of("a", "a", "b").stream().sorted().toList(), List.of("b", "a", "b").stream().sorted().toList());  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> actual = List.of("viewer", "admin", "viewer");
            assertEquals(frequencies(List.of("viewer", "viewer", "admin")), frequencies(actual));
            Map<String, Long> counts = frequencies(actual);            // {viewer=2, admin=1} in any order
            show("counts", counts);
        }
        {
            List<User> members = List.of(new User(17, "bob"), new User(5, "ann"));
            assertThat(members).extracting(User::name).containsExactlyInAnyOrder("ann", "bob");
            List<String> names = members.stream().map(User::name).sorted().toList();   // [ann, bob]
            show("names", names);
            assertEquals(List.of("ann", "bob"), names);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
