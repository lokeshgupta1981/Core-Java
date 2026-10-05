package com.howtodoinjava.stackoverflow.tree;

import java.util.ArrayList;
import java.util.List;

public final class Folder {

  private final String name;
  private final List<Folder> children = new ArrayList<>();

  public Folder(String name) {
    this.name = name;
  }

  public Folder add(Folder child) {
    children.add(child);
    return this;
  }

  public String name() {
    return name;
  }

  public List<Folder> children() {
    return children;
  }

  /** Builds a chain root/f1/f2/.../f(depth-1): depth folders in total, each nested in the previous one. */
  public static Folder chain(int depth) {
    Folder root = new Folder("root");
    Folder current = root;
    for (int i = 1; i < depth; i++) {
      Folder next = new Folder("f" + i);
      current.add(next);
      current = next;
    }
    return root;
  }
}
