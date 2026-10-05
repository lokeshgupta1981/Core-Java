package com.howtodoinjava.stackoverflow.tree;

import java.util.ArrayDeque;
import java.util.Deque;

public final class FolderCounter {

  private FolderCounter() {
  }

  // Correct recursion: one stack frame per nesting level
  public static int countRecursive(Folder folder) {
    int count = 1;
    for (Folder child : folder.children()) {
      count += countRecursive(child);
    }
    return count;
  }

  // Same traversal with an explicit Deque on the heap
  public static int countWithDeque(Folder root) {
    Deque<Folder> pending = new ArrayDeque<>();
    pending.push(root);
    int count = 0;
    while (!pending.isEmpty()) {
      Folder folder = pending.pop();
      count++;
      for (Folder child : folder.children()) {
        pending.push(child);
      }
    }
    return count;
  }
}
