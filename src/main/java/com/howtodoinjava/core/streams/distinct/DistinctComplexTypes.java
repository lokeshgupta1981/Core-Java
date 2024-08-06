package com.howtodoinjava.core.streams.distinct;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DistinctComplexTypes {
  @SuppressWarnings("unchecked")
  public static void main(final String[] args) {
    Person lokeshOne = new Person(1, "Lokesh", "Gupta", "Delhi");
    Person lokeshTwo = new Person(2, "Lokesh", "Gupta", "Noida");
    Person lokeshThree = new Person(3, "Lokesh", "Gupta", "Delhi");
    Person brianOne = new Person(4, "Brian", "Clooney", "New York");
    Person brianTwo = new Person(5, "Brian", "Clooney", "London");
    Person alex = new Person(6, "Alex", "Kolen", "London");

    // Add some random persons
    Collection<Person> list = Arrays.asList(alex, brianOne, brianTwo, lokeshOne,
      lokeshTwo, lokeshThree);

    // 1 - Using default equals() methods

    // Get distinct people by id
    List<Person> distinctElements = list.stream()
      .distinct()
      .collect(Collectors.toList());

    // Let's verify distinct people
    System.out.println(distinctElements);

    // 2 - Using custom equality

    // Get distinct objects by key
    distinctElements = list.stream()
      .filter(distinctByKeys(Person::firstName, Person::lastName))
      .collect(Collectors.toList());

    // Again verify distinct people
    System.out.println(distinctElements);

    // 3 Custom Key class

    distinctElements = list.stream()
      .filter(distinctByKeyClass(CustomKey::new))
      .collect(Collectors.toList());

    // Again verify distinct people
    System.out.println(distinctElements);
  }

  // Utility function
  @SuppressWarnings("unchecked")
  private static <T> Predicate<T> distinctByKeys(final Function<? super T, ?>... keyExtractors) 
  {
    // Set doesn't need to be `concurrent` unless using parallel streams.
    final Set<List<?>> seen = new HashSet<>();
     
    return t -> 
    {
      final List<?> keys = Arrays.stream(keyExtractors)
                  .map(ke -> ke.apply(t))
                  .collect(Collectors.toList());
       
      return seen.add(keys);
    };
  }
  
  public static <T> Predicate<T> distinctByKeyClass(final Function<? super T, Object> keyExtractor) 
  {
    // Set doesn't need to be `concurrent` unless using parallel streams.
    Set<Object> seen = new HashSet<>();
    // Set:add returns true if this set didn't already contain the specified value.
    return t -> seen.add(keyExtractor.apply(t));
  }
}

record Person(Integer id, String firstName, String lastName, String email) {
}

record CustomKey(String firstName, String lastName) {
  public CustomKey(final Person p) 
  {
    this(p.firstName(), p.lastName());
  }
}
