package com.howtodoinjava.datastructure;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomList<E> {

  private int size = 0;
  private static final int DEFAULT_CAPACITY = 16;
  private Object elements[];

  public CustomList() {
    elements = new Object[DEFAULT_CAPACITY];
  }

  public void add(E e) {
    if (size == elements.length) {
      ensureCapacity();
    }
    elements[size++] = e;
  }

  @SuppressWarnings("unchecked")
  public E get(int i) {
    if (i >= size || i < 0) {
      throw new IndexOutOfBoundsException("Index: " + i + ", Size " + i);
    }
    return (E) elements[i];
  }

  public E remove(int i) {
    if (i >= size || i < 0) {
      throw new IndexOutOfBoundsException("Index: " + i + ", Size " + i);
    }
    Object item = elements[i];
    int numElts = elements.length - (i + 1);
    System.arraycopy(elements, i + 1, elements, i, numElts);
    size--;
    return (E) item;
  }

  public int size() {
    return size;
  }

  private void ensureCapacity() {
    int newSize = elements.length * 2;
    elements = Arrays.copyOf(elements, newSize);
  }

  @Override
  public String toString() {
    return "CustomList: [" + Arrays.stream(elements)
        .filter(o -> Objects.nonNull(o))
        .map(o -> o.toString())
        .collect(Collectors.joining(",")) + "]";
  }
}