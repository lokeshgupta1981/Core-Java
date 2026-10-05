package com.howtodoinjava.core.collections.map;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Examples for designing a good HashMap key: records as keys, a mutable key that gets lost after a change,
 * a key that overrides only equals(), a key class whose hashCode() uses only a final field, String hash
 * collisions, EnumMap and IdentityHashMap.
 */
public class HashMapKeyDesign {

  /** A good key: immutable, equals() and hashCode() generated from both components. */
  record Person(String firstName, String lastName) {
  }

  /** A bad key: hashCode() depends on a field that has a setter. */
  static class MutablePerson {
    private String name;

    MutablePerson(String name) {
      this.name = name;
    }

    void setName(String name) {
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      return o instanceof MutablePerson other && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(name);
    }
  }

  /** A broken key: overrides equals() but not hashCode(). */
  static class EqualsOnlyPerson {
    private final String name;

    EqualsOnlyPerson(String name) {
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      return o instanceof EqualsOnlyPerson other && name.equals(other.name);
    }
  }

  /** A record with a mutable component (List) is not deeply immutable. */
  record Team(String name, List<String> members) {
  }

  /** A record that copies its List component, so the key cannot change. */
  record SafeTeam(String name, List<String> members) {
    SafeTeam {
      members = List.copyOf(members);
    }
  }

  /** Mutable class; equals() and hashCode() use only the final account number. */
  static class Account {
    private final int accountNumber;
    private String holderName;

    Account(int accountNumber) {
      this.accountNumber = accountNumber;
    }

    String getHolderName() {
      return holderName;
    }

    void setHolderName(String holderName) {
      this.holderName = holderName;
    }

    int getAccountNumber() {
      return accountNumber;
    }

    @Override
    public int hashCode() {
      return Integer.hashCode(accountNumber);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      Account other = (Account) obj;
      return accountNumber == other.accountNumber;
    }
  }

  public static void main(String[] args) {
    // 1. A record as the key
    Map<Person, Integer> ages = new HashMap<>();
    ages.put(new Person("Lokesh", "Gupta"), 37);
    System.out.println("record get: " + ages.get(new Person("Lokesh", "Gupta")));

    // 2. A mutable key gets lost
    MutablePerson key = new MutablePerson("John");
    Map<MutablePerson, Integer> map = new HashMap<>();
    map.put(key, 40);
    System.out.println("before change: " + map.get(key));
    int oldHash = key.hashCode();
    key.setName("Alex");
    System.out.println("hash John=" + oldHash + " Alex=" + key.hashCode());
    System.out.println("after change get(key): " + map.get(key));
    System.out.println("after change get(new John): " + map.get(new MutablePerson("John")));
    System.out.println("containsKey(key): " + map.containsKey(key));
    System.out.println("remove(key): " + map.remove(key));
    System.out.println("size: " + map.size());
    System.out.println("values: " + map.values());

    // 3. equals() without hashCode()
    Map<EqualsOnlyPerson, Integer> broken = new HashMap<>();
    broken.put(new EqualsOnlyPerson("Lokesh"), 37);
    System.out.println("equals-only equals: "
        + new EqualsOnlyPerson("Lokesh").equals(new EqualsOnlyPerson("Lokesh")));
    System.out.println("equals-only get: " + broken.get(new EqualsOnlyPerson("Lokesh")));
    broken.put(new EqualsOnlyPerson("Lokesh"), 38);
    System.out.println("equals-only size: " + broken.size());

    // 4. A record with a mutable List component
    List<String> members = new ArrayList<>(List.of("Lokesh"));
    Team team = new Team("blue", members);
    Map<Team, Integer> scores = new HashMap<>();
    scores.put(team, 10);
    members.add("John");
    System.out.println("record with list get: " + scores.get(team));
    System.out.println("record with list size: " + scores.size());

    List<String> safeMembers = new ArrayList<>(List.of("Lokesh"));
    SafeTeam safeTeam = new SafeTeam("blue", safeMembers);
    Map<SafeTeam, Integer> safeScores = new HashMap<>();
    safeScores.put(safeTeam, 10);
    safeMembers.add("John");
    System.out.println("safe record get: " + safeScores.get(safeTeam));
    try {
      safeTeam.members().add("Alex");
    } catch (UnsupportedOperationException e) {
      System.out.println("safe record members().add: " + e.getClass().getSimpleName());
    }

    // 5. Mutable class with hashCode() on a final field (Account demo)
    HashMap<Account, String> accounts = new HashMap<>();
    Account a1 = new Account(1);
    a1.setHolderName("A_ONE");
    Account a2 = new Account(2);
    a2.setHolderName("A_TWO");
    accounts.put(a1, a1.getHolderName());
    accounts.put(a2, a2.getHolderName());
    a1.setHolderName("Defaulter");
    a2.setHolderName("Bankrupt");
    System.out.println(accounts.get(a1));
    System.out.println(accounts.get(a2));
    Account a3 = new Account(1);
    a3.setHolderName("A_THREE");
    System.out.println(accounts.get(a3));

    // 6. Hash collisions are normal
    System.out.println("\"Aa\".hashCode() = " + "Aa".hashCode());
    System.out.println("\"BB\".hashCode() = " + "BB".hashCode());
    Map<String, Integer> collide = new HashMap<>();
    collide.put("Aa", 1);
    collide.put("BB", 2);
    System.out.println("Aa -> " + collide.get("Aa") + ", BB -> " + collide.get("BB"));
    System.out.println("Objects.hash(\"Lokesh\", \"Gupta\") = " + Objects.hash("Lokesh", "Gupta"));
    System.out.println("Person record hashCode = " + new Person("Lokesh", "Gupta").hashCode());

    // 7. Enum keys and identity keys
    Map<DayOfWeek, String> plan = new EnumMap<>(DayOfWeek.class);
    plan.put(DayOfWeek.FRIDAY, "gym");
    plan.put(DayOfWeek.MONDAY, "swim");
    System.out.println("EnumMap: " + plan);

    String k1 = new String("apple");
    String k2 = new String("apple");
    Map<String, Integer> byEquals = new HashMap<>();
    byEquals.put(k1, 5);
    byEquals.put(k2, 3);
    Map<String, Integer> byIdentity = new IdentityHashMap<>();
    byIdentity.put(k1, 5);
    byIdentity.put(k2, 3);
    System.out.println("HashMap: " + byEquals + ", IdentityHashMap size: " + byIdentity.size());
    System.out.println("IdentityHashMap get(\"apple\"): " + byIdentity.get("apple"));
  }
}
