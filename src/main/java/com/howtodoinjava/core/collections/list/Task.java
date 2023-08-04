package com.howtodoinjava.core.collections.list;

public record Task(long id, String name, boolean status)
    implements Comparable<Task> {

  @Override
  public int compareTo(Task other) {
    return Long.compare(this.id, other.id);
  }
}
